package com.SharedCheksMercadoPagoIntegration.Controllers;

import com.SharedCheksMercadoPagoIntegration.Entities.SubscribeOrder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/verify-subscription")
public class SubscriptionController {


    // <>-------------- Methods --------------<>

    @GetMapping("/verify-if-have-active-subscription/{email}")
    public String verifyIfHaveActiveSubscription(@PathVariable String email) {
        List<SubscribeOrder> subscriptionPaidRepo = subscriptionPendentRepo.findByEmail(email);


        return ;
    }


}
