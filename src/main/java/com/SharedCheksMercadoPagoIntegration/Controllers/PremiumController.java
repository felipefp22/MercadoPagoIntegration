package com.SharedCheksMercadoPagoIntegration.Controllers;

import com.SharedCheksMercadoPagoIntegration.Servicies.PremiumService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/verify-subscription")
public class PremiumController {
    private final PremiumService premiumService;

    public PremiumController(PremiumService premiumService) {
        this.premiumService = premiumService;
    }

    // <>-------------- Methods --------------<>

    @GetMapping("/verify-if-have-active-subscription/{email}")
    public String verifyIfHaveActiveSubscription(@PathVariable String email) {

        return premiumService.verifyIfHaveActiveSubscription(email);
    }

}
