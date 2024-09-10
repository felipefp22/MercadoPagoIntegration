package com.SharedCheksMercadoPagoIntegration.Servicies;

import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.MerchantOrders.MerchantOrdersDTOs.MerchantOrderDTO;
import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.MerchantOrders.MerchantOrdersDTOs.MerchantOrdersThroughElementsDTO;
import com.SharedCheksMercadoPagoIntegration.Entities.SubscribeOrderPaidAndActive;
import com.SharedCheksMercadoPagoIntegration.Entities.SubscribeOrderPaidAndExpired;
import com.SharedCheksMercadoPagoIntegration.Entities.SubscribeOrderPendind;
import com.SharedCheksMercadoPagoIntegration.Repositories.SubscriptionPaidAndActiveRepo;
import com.SharedCheksMercadoPagoIntegration.Repositories.SubscriptionPaidAndExpiredRepo;
import com.SharedCheksMercadoPagoIntegration.Repositories.SubscriptionPendentRepo;
import jakarta.transaction.Transactional;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import static com.SharedCheksMercadoPagoIntegration.Infra.webRequest.WebClientLinkRequestMP.requisitionGenericMP;
import static com.SharedCheksMercadoPagoIntegration.Infra.webRequest.WebClientLinkRequestSharedChecks.requisitionGenericSharedChecks;

@Service
public class SubscriptionService {

    private final SubscriptionPendentRepo subscriptionPendentRepo;
    private final SubscriptionPaidAndActiveRepo subscriptionPaidAndActiveRepo;
    private final SubscriptionPaidAndExpiredRepo subscriptionPaidAndExpiredRepo;

    public SubscriptionService(SubscriptionPendentRepo subscriptionPendentRepo, SubscriptionPaidAndActiveRepo subscriptionPaidAndActiveRepo, SubscriptionPaidAndActiveRepo subscriptionPaidAndActiveRepo1, SubscriptionPaidAndExpiredRepo subscriptionPaidAndExpiredRepo) {
        this.subscriptionPendentRepo = subscriptionPendentRepo;
        this.subscriptionPaidAndActiveRepo = subscriptionPaidAndActiveRepo1;
        this.subscriptionPaidAndExpiredRepo = subscriptionPaidAndExpiredRepo;
    }

    // <>-------------- Methods --------------<>

    public String verifyIfHaveActiveSubscription(String email) {
        verifyWithMpIfHadNewPayment();

        SubscribeOrderPaidAndActive subscriptionActive = subscriptionPaidAndActiveRepo.findByEmailProfileID(email)
                .stream().findFirst().orElse(null);

        if (subscriptionActive != null && subscriptionActive.getValidTillUTC().isAfter(LocalDateTime.now())) {
            // Activate User subscription in main API
            activateSubscriptionForUserMainAPI(subscriptionActive);

            return "Desculpe pelo incoveniente, encontramos sua assinatura ativa, e já a ativamos para você!";
        } else {
            SubscribeOrderPendind subscriptionPending = subscriptionPendentRepo.findByEmailProfileID(email)
                    .stream().findFirst().orElseThrow(() -> new RuntimeException("No subscription found"));

            var externalReference = subscriptionPending.getOrderID();

            MerchantOrdersThroughElementsDTO merchantOrderElements =
                    requisitionGenericMP("/merchant_orders/search?external_reference=" + externalReference.toString()
                            , HttpMethod.GET, null,
                            new ParameterizedTypeReference<MerchantOrdersThroughElementsDTO>() {
                            }, null);

//            MerchantOrder merchantOrder = merchantOrderElements.elements().stream().findFirst().orElse(null);
//
//            if (merchantOrder.getPayments().stream().findFirst().orElse(null).status().equals("approved")) {
//                // Move pending subscription to paid and activate subscription
//                movePendingSubscriptionToPaidAndActivateSubscription(subscriptionPending, merchantOrder);
//
//                return "Desculpe pelo incoveniente, encontramos sua assinatura ativa, e já a ativamos para você!";
//            } else {
            return "No subscription found";
//            }
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
        subscriptionPendentRepo.deleteById(subscribeOrderToActivate.getOrderID());

        // Activate User subscription in main API
        activateSubscriptionForUserMainAPI(subscribeOrderToActivate);
    }

    private void activateSubscriptionForUserMainAPI(SubscribeOrderPaidAndActive subscribeOrderPaidAndActive) {

        requisitionGenericSharedChecks("",
                HttpMethod.POST, null,
                new ParameterizedTypeReference<Object>() {
                }, null);
    }

    private LocalDateTime takeLocalDateTimeDatePayApprovedUTC(String dateApprovedString) {
        ZonedDateTime zonedDateTimeDatePayApproved = ZonedDateTime.parse(dateApprovedString);

        ZonedDateTime utcZonedDateTimeDatePayApproved = zonedDateTimeDatePayApproved.withZoneSameInstant(ZoneId.of("UTC"));
        return utcZonedDateTimeDatePayApproved.toLocalDateTime();
    }

    // <>-------------- Routines --------------<>
    @Scheduled(fixedDelay = 6000000)
    public void verifyWithMpIfHadNewPayment() {

        List<SubscribeOrderPendind> subscriptionsPendind = subscriptionPendentRepo.findAll();

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
//                requisitionGenericSharedChecks("",
//                        HttpMethod.DELETE, null,
//                        new ParameterizedTypeReference<Object>() {
//                        }, null);
            }
        });
    }
}
