package com.SharedCheksMercadoPagoIntegration.Servicies;

import com.SharedCheksMercadoPagoIntegration.Entities.SubscribeOrder;
import com.SharedCheksMercadoPagoIntegration.Repositories.SubscriptionPaidRepo;
import com.SharedCheksMercadoPagoIntegration.Repositories.SubscriptionPendentRepo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionService {

    private final SubscriptionPendentRepo subscriptionPendentRepo;
    private final SubscriptionPaidRepo subscriptionPaidRepo;

    public SubscriptionService(SubscriptionPendentRepo subscriptionPendentRepo, SubscriptionPaidRepo subscriptionPaidRepo) {
        this.subscriptionPendentRepo = subscriptionPendentRepo;
        this.subscriptionPaidRepo = subscriptionPaidRepo;
    }

    // <>-------------- Methods --------------<>

    public String verifyIfHaveActiveSubscription(String email) {
        verifyWithMpIfHadPayment();

        List<SubscribeOrder> subscriptionPaidRepo = subscriptionPendentRepo.findByEmail(email);

        return;
    }

    // <>-------------- Aux Methods --------------<>
    @Scheduled(fixedDelay = 10000)
    private void verifyWithMpIfHadPayment() {
        List<SubscribeOrder> subscriptionPaidRepo = subscriptionPendentRepo.findAll();


    }
}
