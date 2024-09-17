package com.SharedCheksMercadoPagoIntegration.Controllers;

import com.SharedCheksMercadoPagoIntegration.Servicies.SubscriptionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/verify-subscription")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    // <>-------------- Methods --------------<>

    @GetMapping("/verify-if-have-active-subscription/{email}")
    public String verifyIfHaveActiveSubscription(@PathVariable String email) {

        return subscriptionService.verifyIfHaveActiveSubscription(email);
    }

}
