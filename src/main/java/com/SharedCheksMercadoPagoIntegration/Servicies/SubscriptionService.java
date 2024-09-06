package com.SharedCheksMercadoPagoIntegration.Servicies;

import com.SharedCheksMercadoPagoIntegration.Entities.SubscribeOrder;
import com.SharedCheksMercadoPagoIntegration.Repositories.SubscriptionPaidAndActiveRepo;
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

    public SubscriptionService(SubscriptionPendentRepo subscriptionPendentRepo, SubscriptionPaidAndActiveRepo subscriptionPaidAndActiveRepo, SubscriptionPaidAndActiveRepo subscriptionPaidAndActiveRepo1) {
        this.subscriptionPendentRepo = subscriptionPendentRepo;
        this.subscriptionPaidAndActiveRepo = subscriptionPaidAndActiveRepo1;
    }

    // <>-------------- Methods --------------<>

    public String verifyIfHaveActiveSubscription(String email) {
        verifyWithMpIfHadPayment();

        SubscribeOrder subscriptionActive = subscriptionPaidAndActiveRepo.findByEmailProfileID(email)
                .stream().findFirst().orElse(null);

        if (subscriptionActive != null && subscriptionActive.getValidTill().isAfter(LocalDateTime.now())) {
            activateSubscriptionForUserMainAPI(subscriptionActive);

            return "Desculpe pelo incoveniente, encontramos sua assinatura ativa, e já a ativamos para você!";
        }else {
            SubscribeOrder subscriptionPending = subscriptionPendentRepo.findByEmailProfileID(email)
                    .stream().findFirst().orElseThrow(() -> new RuntimeException("No subscription found"));

            var externalReference = subscriptionPending.getOrderID();

            Object merchantOrder =
                    requisitionGeneric("/merchant_orders/search?external_reference=" + externalReference.toString()
                            , HttpMethod.GET, null,
                            new ParameterizedTypeReference<Object>() {
                            }, null);

            if (merchantOrder.payments().status().equals("approved")) {
                movePendingSubscriptionToPaidAndActivateSubscription(subscriptionPending, merchantOrder);
            }

            return "Desculpe pelo incoveniente, encontramos sua assinatura ativa, e já a ativamos para você!";
        }

        return "No subscription found";
    }

    // <>-------------- Aux Methods --------------<>


    @Transactional
    private void movePendingSubscriptionToPaidAndActivateSubscription(SubscribeOrder subscribeOrderPendingToChange,
                                                                      Object merchantOrder) {

        subscribeOrderPendingToChange.setStatus("PAID");
        subscribeOrderPendingToChange.setPaidAt(merchantOrder.payments().date_approved());
        subscribeOrderPendingToChange.setValidTill(merchantOrder.payments().date_approved()
                .plusdays(subscribeOrderPendingToChange.getKindOfSubscription().getDays()));

        var subscribeOrderToActivate = subscriptionPaidAndActiveRepo.save(subscribeOrderPendingToChange);
        subscriptionPendentRepo.deleteById(subscribeOrderPendingToChange.getOrderID());

        activateSubscriptionForUserMainAPI(subscribeOrderToActivate);


    }

    private void activateSubscriptionForUserMainAPI(SubscribeOrder subscribeOrder) {
        Object returnOfMP =
                requisitionGeneric("
                        , HttpMethod.Post, null,
                        new ParameterizedTypeReference<Object>() {
                        }, null);
    }

    // <>-------------- Routines --------------<>
    @Scheduled(fixedDelay = 10000)
    private void verifyWithMpIfHadPayment() {
        List<SubscribeOrder> S = subscriptionPendentRepo.findAll();

        Object returnOfMP =
                requisitionGeneric("/merchant_orders/search?external_reference=" + externalReference
                        , HttpMethod.GET, null,
                        new ParameterizedTypeReference<Object>() {
                        }, null);

    }

//
//    @Scheduled(fixedDelay = 10000)
//    @Transactional
//    private void takingOutExpiredSubscriptions() {
//        List<SubscribeOrder> subscriptionPaidRepo = subscriptionPaidRepo
//
//    }

}
