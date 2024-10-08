package com.SharedCheksMercadoPagoIntegration.WebHook;

import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.MerchantOrders.MerchantOrdersDTOs.MerchantOrderDTO;
import com.SharedCheksMercadoPagoIntegration.Entities.PremiumOrderPendind;
import com.SharedCheksMercadoPagoIntegration.Repositories.PremiumPendingRepo;
import com.SharedCheksMercadoPagoIntegration.Servicies.PremiumService;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.SharedCheksMercadoPagoIntegration.Infra.webRequest.WebClientLinkRequestMP.requisitionGenericMP;

@RestController
@RequestMapping("/webhook-receives")
public class WebHookReceivesController {

    @Value("${mp.webhook.signature}")
    private String mpWebhookSignature;

    private final PremiumService premiumService;
    private final PremiumPendingRepo premiumPendingRepo;

    public WebHookReceivesController(PremiumService premiumService, PremiumPendingRepo premiumPendingRepo) {
        this.premiumService = premiumService;
        this.premiumPendingRepo = premiumPendingRepo;
    }

    // <>-------------- Methods --------------<>

    @PostMapping("/mp-payments")
    public ResponseEntity receiveMpPayments(@RequestBody WebHookDTO webHookDTO,
                                            @RequestHeader("X-Signature") String xSignature,
                                            @RequestHeader("X-Request-Id") String xRequestId,
                                            @RequestParam("data.id") String dataId) {

        //boolean isValid = isSignatureValid(xSignature, xRequestId, dataId);

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

                premiumService.movePendingpremiumToPaidAndActivatePremium(subscriptionsPendind, merchantOrderDTO);
            }
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }


    // <>-------------- Private Methods --------------<>

    private boolean isSignatureValid(String xSignature, String xRequestId, String dataId) {
        // Extrair os valores ts e v1 do header x-signature
        String mpTs = xSignature.split(",")[0].split("=")[1];
        String mpV1 = xSignature.split(",")[1].split("=")[1];

        String signedTemplate = "id:" + dataId + ";request-id:" + xRequestId + ";ts:" + mpTs;

        String generatedSignature = new HmacUtils("HmacSHA256", mpWebhookSignature).hmacHex(signedTemplate);

        return generatedSignature.equals(mpV1);
    }
}
