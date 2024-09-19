package com.SharedCheksMercadoPagoIntegration.Servicies;

import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.MerchantOrders.MerchantOrdersDTOs.MerchantOrderDTO;
import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.MerchantOrders.MerchantOrdersDTOs.MerchantOrdersThroughElementsDTO;
import com.SharedCheksMercadoPagoIntegration.Entities.SubscribeOrderPaidAndActive;
import com.SharedCheksMercadoPagoIntegration.Entities.SubscribeOrderPaidAndExpired;
import com.SharedCheksMercadoPagoIntegration.Entities.SubscribeOrderPendind;
import com.SharedCheksMercadoPagoIntegration.Repositories.SubscriptionPaidAndActiveRepo;
import com.SharedCheksMercadoPagoIntegration.Repositories.SubscriptionPaidAndExpiredRepo;
import com.SharedCheksMercadoPagoIntegration.Repositories.SubscriptionPendingRepo;
import jakarta.transaction.Transactional;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;

import static com.SharedCheksMercadoPagoIntegration.Infra.webRequest.WebClientLinkRequestMP.requisitionGenericMP;
import static com.SharedCheksMercadoPagoIntegration.Infra.webRequest.WebClientLinkRequestSharedChecks.requisitionGenericSharedChecks;

@Service
public class SubscriptionService {

    private final SubscriptionPendingRepo subscriptionPendingRepo;
    private final SubscriptionPaidAndActiveRepo subscriptionPaidAndActiveRepo;
    private final SubscriptionPaidAndExpiredRepo subscriptionPaidAndExpiredRepo;

    public SubscriptionService(SubscriptionPendingRepo subscriptionPendingRepo, SubscriptionPaidAndActiveRepo subscriptionPaidAndActiveRepo, SubscriptionPaidAndActiveRepo subscriptionPaidAndActiveRepo1, SubscriptionPaidAndExpiredRepo subscriptionPaidAndExpiredRepo) {
        this.subscriptionPendingRepo = subscriptionPendingRepo;
        this.subscriptionPaidAndActiveRepo = subscriptionPaidAndActiveRepo1;
        this.subscriptionPaidAndExpiredRepo = subscriptionPaidAndExpiredRepo;
    }

    // <>-------------- Methods --------------<>

    public String verifyIfHaveActiveSubscription(String email) {
        verifyWithMpIfHadNewPaymentEspecificUser(email);

        SubscribeOrderPaidAndActive subscriptionActive = subscriptionPaidAndActiveRepo.findByEmailProfileID(email)
                .stream().findFirst().orElse(null);

        if (subscriptionActive != null && subscriptionActive.getValidTillUTC().isAfter(LocalDateTime.now(ZoneOffset.UTC))) {
            // Activate User subscription in main API
            activateSubscriptionForUserMainAPI(subscriptionActive);

            return "Desculpe pelo incoveniente, encontramos sua assinatura ativa, e já a ativamos para você!";
        } else {
            SubscribeOrderPendind subscriptionPending = subscriptionPendingRepo.findByEmailProfileID(email)
                    .stream().findFirst().orElseThrow(() -> new RuntimeException("No subscription found"));

            var externalReference = subscriptionPending.getOrderID();

            MerchantOrdersThroughElementsDTO merchantOrderElements =
                    requisitionGenericMP("/merchant_orders/search?external_reference=" + externalReference.toString()
                            , HttpMethod.GET, null,
                            new ParameterizedTypeReference<MerchantOrdersThroughElementsDTO>() {
                            }, null);

            return "No subscription found";
        }
    }

    // <>-------------- Aux Methods --------------<>


    @Transactional
    private void movePendingSubscriptionToPaidAndActivateSubscription(SubscribeOrderPendind subscribeOrderPendindPendingToChange,
                                                                      MerchantOrderDTO merchantOrderDTO) {
        var subscribeOrderPaidAndActiveToSave = new SubscribeOrderPaidAndActive(subscribeOrderPendindPendingToChange);

        subscribeOrderPaidAndActiveToSave.setStatus("PAID");
        subscribeOrderPaidAndActiveToSave.setPaidAtUTC(
                takeLocalDateTimeDatePayApprovedUTC(merchantOrderDTO.payments().stream().findFirst().orElse(null).date_approved()));
        subscribeOrderPaidAndActiveToSave.setValidTillUTC(
                takeLocalDateTimeDatePayApprovedUTC(merchantOrderDTO.payments().stream().findFirst().orElse(null).date_approved())
                        .plusDays(subscribeOrderPendindPendingToChange.getKindOfSubscription().getDays()));

        subscribeOrderPaidAndActiveToSave.setMerchantOrderFromDTO(merchantOrderDTO);

        // Persisting in DB
        SubscribeOrderPaidAndActive subscribeOrderToActivate = subscriptionPaidAndActiveRepo.save(subscribeOrderPaidAndActiveToSave);
        subscriptionPendingRepo.deleteById(subscribeOrderToActivate.getOrderID());

        // Activate User subscription in main API
        activateSubscriptionForUserMainAPI(subscribeOrderToActivate);
    }

    public void verifyWithMpIfHadNewPaymentEspecificUser(String emailID) {

        SubscribeOrderPendind subscriptionsPendind =
                subscriptionPendingRepo.findByEmailProfileID(emailID).stream().findFirst().orElse(null);

        if (subscriptionsPendind != null) {
            MerchantOrdersThroughElementsDTO merchantOrderElements =
                    requisitionGenericMP(
                            "/merchant_orders/search?external_reference=" + subscriptionsPendind.getOrderID().toString(),
                            HttpMethod.GET, null,
                            new ParameterizedTypeReference<MerchantOrdersThroughElementsDTO>() {
                            }, null);

            MerchantOrderDTO merchantOrderDTO = merchantOrderElements.elements().stream().findFirst().orElse(null);

            if (merchantOrderDTO.payments().stream().findFirst().orElse(null).status().equals("approved")) {
                movePendingSubscriptionToPaidAndActivateSubscription(subscriptionsPendind, merchantOrderDTO);
            }
        }
    }

    private void activateSubscriptionForUserMainAPI(SubscribeOrderPaidAndActive subscribeOrderPaidAndActive) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("validUntillUTC", subscribeOrderPaidAndActive.getValidTillUTC().toString());
        headers.put("userID", subscribeOrderPaidAndActive.getEmailProfileID());

        requisitionGenericSharedChecks("/internal-subscription-actions/activate-subscription",
                HttpMethod.POST, null,
                new ParameterizedTypeReference<>() {
                },
                headers);
    }

    private LocalDateTime takeLocalDateTimeDatePayApprovedUTC(String dateApprovedString) {
        ZonedDateTime zonedDateTimeDatePayApproved = ZonedDateTime.parse(dateApprovedString);

        ZonedDateTime utcZonedDateTimeDatePayApproved = zonedDateTimeDatePayApproved.withZoneSameInstant(ZoneId.of("UTC"));
        return utcZonedDateTimeDatePayApproved.toLocalDateTime();
    }

    // <>-------------- Routines --------------<>
    @Scheduled(fixedDelay = 6000000)
    public void verifyWithMpIfHadNewPayment() {

        List<SubscribeOrderPendind> subscriptionsPendind = subscriptionPendingRepo.findAll();

        subscriptionsPendind.forEach(x -> {
            MerchantOrdersThroughElementsDTO merchantOrderElements =
                    requisitionGenericMP("/merchant_orders/search?external_reference=" + x.getOrderID().toString(),
                            HttpMethod.GET, null,
                            new ParameterizedTypeReference<MerchantOrdersThroughElementsDTO>() {
                            }, null);

            MerchantOrderDTO merchantOrderDTO = merchantOrderElements.elements().stream().findFirst().orElse(null);

            if (merchantOrderDTO.payments().stream().findFirst().orElse(null).status().equals("approved")) {

                movePendingSubscriptionToPaidAndActivateSubscription(x, merchantOrderDTO);
            }
        });
    }


    @Scheduled(fixedDelay = 21600000)
    @Transactional
    public void takingOutExpiredSubscriptions() {
        List<SubscribeOrderPaidAndActive> subscriptionPaidRepo = subscriptionPaidAndActiveRepo.findAll();

        subscriptionPaidRepo.forEach(x -> {
            if (x.getValidTillUTC().isBefore(LocalDateTime.now(ZoneOffset.UTC))) {
                x.setStatus("PAIDANDEXPIRED");
                var subscriptionPaidAndExpiredSaved = subscriptionPaidAndExpiredRepo.save(new SubscribeOrderPaidAndExpired(x));
                subscriptionPaidAndActiveRepo.deleteById(subscriptionPaidAndExpiredSaved.getOrderID());

                // Deactivate User subscription in main API
//                requisitionGenericSharedChecks("/internal-subscription-actions/deactivate-subscription",
//                        HttpMethod.DELETE, null,
//                        new ParameterizedTypeReference<Object>() {
//                        }, null);
            }
        });
    }
}
