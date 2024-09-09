package com.SharedCheksMercadoPagoIntegration.Servicies;

import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.MerchantOrders.MerchantOrder;
import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.MerchantOrders.MerchantOrdersDTOs.MerchantOrdersThroughElementsDTO;
import com.SharedCheksMercadoPagoIntegration.Entities.SubscribeOrder;
import com.SharedCheksMercadoPagoIntegration.Repositories.SubscriptionPaidAndActiveRepo;
import com.SharedCheksMercadoPagoIntegration.Repositories.SubscriptionPaidAndExpiredRepo;
import com.SharedCheksMercadoPagoIntegration.Repositories.SubscriptionPendentRepo;
import jakarta.transaction.Transactional;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.SharedCheksMercadoPagoIntegration.Infra.webRequest.WebClientLinkRequest.requisitionGeneric;

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

        SubscribeOrder subscriptionActive = subscriptionPaidAndActiveRepo.findByEmailProfileID(email)
                .stream().findFirst().orElse(null);

        if (subscriptionActive != null && subscriptionActive.getValidTill().isAfter(LocalDateTime.now())) {
            // Activate User subscription in main API
            activateSubscriptionForUserMainAPI(subscriptionActive);

            return "Desculpe pelo incoveniente, encontramos sua assinatura ativa, e já a ativamos para você!";
        } else {
            SubscribeOrder subscriptionPending = subscriptionPendentRepo.findByEmailProfileID(email)
                    .stream().findFirst().orElseThrow(() -> new RuntimeException("No subscription found"));

            var externalReference = subscriptionPending.getOrderID();

            MerchantOrdersThroughElementsDTO merchantOrderElements =
                    requisitionGeneric("/merchant_orders/search?external_reference=" + externalReference.toString()
                            , HttpMethod.GET, null,
                            new ParameterizedTypeReference<MerchantOrdersThroughElementsDTO>() {
                            }, null);

            MerchantOrder merchantOrder = merchantOrderElements.elements().stream().findFirst().orElse(null);

            if (merchantOrder.getPayments().stream().findFirst().orElse(null).status().equals("approved")) {
                // Move pending subscription to paid and activate subscription
                movePendingSubscriptionToPaidAndActivateSubscription(subscriptionPending, merchantOrder);

                return "Desculpe pelo incoveniente, encontramos sua assinatura ativa, e já a ativamos para você!";
            } else {
                return "No subscription found";
            }
        }
    }

    // <>-------------- Aux Methods --------------<>


    @Transactional
    private void movePendingSubscriptionToPaidAndActivateSubscription(SubscribeOrder subscribeOrderPendingToChange,
                                                                      MerchantOrder merchantOrder) {

        subscribeOrderPendingToChange.setStatus("PAID");
        subscribeOrderPendingToChange.setPaidAt(
                LocalDateTime.parse(merchantOrder.getPayments().stream().findFirst().orElse(null).date_approved()));
        subscribeOrderPendingToChange.setValidTill(
                LocalDateTime.parse(merchantOrder.getPayments().stream().findFirst().orElse(null).date_approved())
                        .plusDays(subscribeOrderPendingToChange.getKindOfSubscription().getDays()));
        //subscribeOrderPendingToChange.setMerchantOrder(merchantOrder);

        var subscribeOrderToActivate = subscriptionPaidAndActiveRepo.save(subscribeOrderPendingToChange);
        subscriptionPendentRepo.deleteById(subscribeOrderPendingToChange.getOrderID());

        // Activate User subscription in main API
        activateSubscriptionForUserMainAPI(subscribeOrderToActivate);
    }

    private void activateSubscriptionForUserMainAPI(SubscribeOrder subscribeOrder) {

        requisitionGeneric("",
                HttpMethod.POST, null,
                new ParameterizedTypeReference<Object>() {
                }, null);
    }

    // <>-------------- Routines --------------<>
    @Scheduled(fixedDelay = 6000000)
    public void verifyWithMpIfHadNewPayment() {
        List<SubscribeOrder> subscriptionsPendind = subscriptionPendentRepo.findAll();

        subscriptionsPendind.forEach(x -> {
            MerchantOrdersThroughElementsDTO merchantOrderElements =
                    requisitionGeneric("/merchant_orders/search?external_reference=" + x.getOrderID().toString(),
                            HttpMethod.GET, null,
                            new ParameterizedTypeReference<MerchantOrdersThroughElementsDTO>() {
                            }, null);

            MerchantOrder merchantOrder = merchantOrderElements.elements().stream().findFirst().orElse(null);

            if (merchantOrder.getPayments().stream().findFirst().orElse(null).status().equals("approved")) {
                movePendingSubscriptionToPaidAndActivateSubscription(x, merchantOrder);
            }
        });
    }


    @Scheduled(fixedDelay = 21600000)
    @Transactional
    public void takingOutExpiredSubscriptions() {
        List<SubscribeOrder> subscriptionPaidRepo = subscriptionPaidAndActiveRepo.findAll();

        subscriptionPaidRepo.forEach(x -> {
            if (x.getValidTill().isBefore(LocalDateTime.now())) {
                x.setStatus("PAIDANDEXPIRED");
                subscriptionPaidAndExpiredRepo.save(x);
                subscriptionPaidAndActiveRepo.deleteById(x.getOrderID());

                // Deactivate User subscription in main API
                requisitionGeneric("",
                        HttpMethod.DELETE, null,
                        new ParameterizedTypeReference<Object>() {
                        }, null);
            }
        });

    }

}
