package com.SharedCheksMercadoPagoIntegration.WebHook;

import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.MerchantOrders.MerchantOrdersDTOs.MerchantOrderDTO;
import com.SharedCheksMercadoPagoIntegration.Entities.PremiumOrderPendind;
import com.SharedCheksMercadoPagoIntegration.Repositories.PremiumPendingRepo;
import com.SharedCheksMercadoPagoIntegration.Servicies.PremiumService;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.SharedCheksMercadoPagoIntegration.Infra.webRequest.WebClientLinkRequestMP.requisitionGenericMP;

@RestController
@RequestMapping("/webhook-receives")
public class WebHookReceivesController {
    private final PremiumService premiumService;
    private final PremiumPendingRepo premiumPendingRepo;

    public WebHookReceivesController(PremiumService premiumService, PremiumPendingRepo premiumPendingRepo) {
        this.premiumService = premiumService;
        this.premiumPendingRepo = premiumPendingRepo;
    }

    // <>-------------- Methods --------------<>

    @PostMapping("/mp-payments")
    public ResponseEntity receiveMpPayments(@RequestBody WebHookDTO webHookDTO) {

        System.out.println(webHookDTO.toString());

        if (webHookDTO.status().equals("closed")) {
            MerchantOrderDTO merchantOrderDTO =
                    requisitionGenericMP("/merchant_orders/" + webHookDTO.id(),
                            HttpMethod.GET, null,
                            new ParameterizedTypeReference<MerchantOrderDTO>() {
                            }, null);

            //MerchantOrderDTO merchantOrderDTO = merchantOrderElements.elements().stream().findFirst().orElse(null);

            PremiumOrderPendind subscriptionsPendind =
                    premiumPendingRepo.findById(UUID.fromString(merchantOrderDTO.external_reference())).orElse(null);

            if (merchantOrderDTO.payments().stream().findFirst().orElse(null).status().equals("approved")) {

                premiumService.movePendingSubscriptionToPaidAndActivateSubscription(subscriptionsPendind, merchantOrderDTO);
            }
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
