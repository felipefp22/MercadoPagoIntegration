package com.SharedCheksMercadoPagoIntegration.Servicies;

import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.MerchantOrders.MerchantOrdersDTOs.MerchantOrderDTO;
import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.MerchantOrders.MerchantOrdersDTOs.MerchantOrdersThroughElementsDTO;
import com.SharedCheksMercadoPagoIntegration.Entities.PremiumOrderPaidAndActive;
import com.SharedCheksMercadoPagoIntegration.Entities.PremiumOrderPaidAndExpired;
import com.SharedCheksMercadoPagoIntegration.Entities.PremiumOrderPendind;
import com.SharedCheksMercadoPagoIntegration.Repositories.PremiumPaidAndActiveRepo;
import com.SharedCheksMercadoPagoIntegration.Repositories.PremiumPaidAndExpiredRepo;
import com.SharedCheksMercadoPagoIntegration.Repositories.PremiumPendingRepo;
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
public class PremiumService {

    private final PremiumPendingRepo premiumPendingRepo;
    private final PremiumPaidAndActiveRepo premiumPaidAndActiveRepo;
    private final PremiumPaidAndExpiredRepo premiumPaidAndExpiredRepo;

    public PremiumService(PremiumPendingRepo premiumPendingRepo, PremiumPaidAndActiveRepo premiumPaidAndActiveRepo, PremiumPaidAndActiveRepo premiumPaidAndActiveRepo1, PremiumPaidAndExpiredRepo premiumPaidAndExpiredRepo) {
        this.premiumPendingRepo = premiumPendingRepo;
        this.premiumPaidAndActiveRepo = premiumPaidAndActiveRepo1;
        this.premiumPaidAndExpiredRepo = premiumPaidAndExpiredRepo;
    }

    // <>-------------- Methods --------------<>

    public String verifyIfHaveActiveSubscription(String email) {
        verifyWithMpIfHadNewPaymentEspecificUser(email);

        PremiumOrderPaidAndActive subscriptionActive = premiumPaidAndActiveRepo.findByEmailProfileID(email)
                .stream().findFirst().orElse(null);

        if (subscriptionActive != null && subscriptionActive.getValidTillUTC().isAfter(LocalDateTime.now(ZoneOffset.UTC))) {
            // Activate User subscription in main API
            activateSubscriptionForUserMainAPI(subscriptionActive);

            return "Desculpe pelo incoveniente, encontramos sua assinatura ativa, e já a ativamos para você!";
        } else {
            PremiumOrderPendind subscriptionPending = premiumPendingRepo.findByEmailProfileID(email)
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
    public void movePendingSubscriptionToPaidAndActivateSubscription(PremiumOrderPendind premiumOrderPendindPendingToChange,
                                                                     MerchantOrderDTO merchantOrderDTO) {
        var subscribeOrderPaidAndActiveToSave = new PremiumOrderPaidAndActive(premiumOrderPendindPendingToChange);

        subscribeOrderPaidAndActiveToSave.setStatus("PAID");
        subscribeOrderPaidAndActiveToSave.setPaidAtUTC(
                takeLocalDateTimeDatePayApprovedUTC(merchantOrderDTO.payments().stream().findFirst().orElse(null).date_approved()));
        subscribeOrderPaidAndActiveToSave.setValidTillUTC(
                takeLocalDateTimeDatePayApprovedUTC(merchantOrderDTO.payments().stream().findFirst().orElse(null).date_approved())
                        .plusDays(premiumOrderPendindPendingToChange.getKindOfSubscription().getDays()));

        subscribeOrderPaidAndActiveToSave.setMerchantOrderFromDTO(merchantOrderDTO);

        // Persisting in DB
        PremiumOrderPaidAndActive subscribeOrderToActivate = premiumPaidAndActiveRepo.save(subscribeOrderPaidAndActiveToSave);
        premiumPendingRepo.deleteById(subscribeOrderToActivate.getOrderID());

        // Activate User subscription in main API
        activateSubscriptionForUserMainAPI(subscribeOrderToActivate);
    }

    public void verifyWithMpIfHadNewPaymentEspecificUser(String emailID) {

        PremiumOrderPendind subscriptionsPendind =
                premiumPendingRepo.findByEmailProfileID(emailID).stream().findFirst().orElse(null);

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

    private void activateSubscriptionForUserMainAPI(PremiumOrderPaidAndActive premiumOrderPaidAndActive) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("validUntillUTC", premiumOrderPaidAndActive.getValidTillUTC().toString());
        headers.put("userID", premiumOrderPaidAndActive.getEmailProfileID());

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

        List<PremiumOrderPendind> subscriptionsPendind = premiumPendingRepo.findAll();

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
        List<PremiumOrderPaidAndActive> subscriptionPaidRepo = premiumPaidAndActiveRepo.findAll();

        subscriptionPaidRepo.forEach(x -> {
            if (x.getValidTillUTC().isBefore(LocalDateTime.now(ZoneOffset.UTC))) {
                x.setStatus("PAIDANDEXPIRED");
                var subscriptionPaidAndExpiredSaved = premiumPaidAndExpiredRepo.save(new PremiumOrderPaidAndExpired(x));
                premiumPaidAndActiveRepo.deleteById(subscriptionPaidAndExpiredSaved.getOrderID());

                // Deactivate User subscription in main API
//                requisitionGenericSharedChecks("/internal-subscription-actions/deactivate-subscription",
//                        HttpMethod.DELETE, null,
//                        new ParameterizedTypeReference<Object>() {
//                        }, null);
            }
        });
    }
}
