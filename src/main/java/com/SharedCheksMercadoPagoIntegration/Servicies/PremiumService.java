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

    public String verifyIfHaveActivePremium(String email) {
        verifyWithMpIfHadNewPaymentEspecificUser(email);

        PremiumOrderPaidAndActive premiumActive = premiumPaidAndActiveRepo.findByEmailProfileID(email)
                .stream().findFirst().orElse(null);

        if (premiumActive != null && premiumActive.getValidTillUTC().isAfter(LocalDateTime.now(ZoneOffset.UTC))) {
            // Activate User premium in main API
            activatePremiumForUserMainAPI(premiumActive);

            return "Desculpe pelo incoveniente, encontramos sua assinatura ativa, e já a ativamos para você!";
        } else {
            PremiumOrderPendind premiumPending = premiumPendingRepo.findByEmailProfileID(email)
                    .stream().findFirst().orElseThrow(() -> new RuntimeException("No premium found"));

            var externalReference = premiumPending.getOrderID();

            MerchantOrdersThroughElementsDTO merchantOrderElements =
                    requisitionGenericMP("/merchant_orders/search?external_reference=" + externalReference.toString()
                            , HttpMethod.GET, null,
                            new ParameterizedTypeReference<MerchantOrdersThroughElementsDTO>() {
                            }, null);

            return "No premium found";
        }
    }

    // <>-------------- Aux Methods --------------<>


    @Transactional
    public void movePendingpremiumToPaidAndActivatePremium(PremiumOrderPendind premiumOrderPendindPendingToChange,
                                                           MerchantOrderDTO merchantOrderDTO) {
        var premiumOrderPaidAndActiveToSave = new PremiumOrderPaidAndActive(premiumOrderPendindPendingToChange);

        premiumOrderPaidAndActiveToSave.setStatus("PAID");
        premiumOrderPaidAndActiveToSave.setPaidAtUTC(
                takeLocalDateTimeDatePayApprovedUTC(merchantOrderDTO.payments().stream().findFirst().orElse(null).date_approved()));
        premiumOrderPaidAndActiveToSave.setValidTillUTC(
                takeLocalDateTimeDatePayApprovedUTC(merchantOrderDTO.payments().stream().findFirst().orElse(null).date_approved())
                        .plusDays(premiumOrderPendindPendingToChange.getKindOfPremium().getDays()));

        premiumOrderPaidAndActiveToSave.setMerchantOrderFromDTO(merchantOrderDTO);

        // Persisting in DB
        PremiumOrderPaidAndActive premiumOrderToActivate = premiumPaidAndActiveRepo.save(premiumOrderPaidAndActiveToSave);
        premiumPendingRepo.deleteById(premiumOrderToActivate.getOrderID());

        // Activate User premium in main API
        activatePremiumForUserMainAPI(premiumOrderToActivate);
    }

    public void verifyWithMpIfHadNewPaymentEspecificUser(String emailID) {

        PremiumOrderPendind premiumPendind =
                premiumPendingRepo.findByEmailProfileID(emailID).stream().findFirst().orElse(null);

        if (premiumPendind != null) {
            MerchantOrdersThroughElementsDTO merchantOrderElements =
                    requisitionGenericMP(
                            "/merchant_orders/search?external_reference=" + premiumPendind.getOrderID().toString(),
                            HttpMethod.GET, null,
                            new ParameterizedTypeReference<MerchantOrdersThroughElementsDTO>() {
                            }, null);

            MerchantOrderDTO merchantOrderDTO = merchantOrderElements.elements().stream().findFirst().orElse(null);

            if (merchantOrderDTO.payments().stream().findFirst().orElse(null).status().equals("approved")) {
                movePendingpremiumToPaidAndActivatePremium(premiumPendind, merchantOrderDTO);
            }
        }
    }

    private void activatePremiumForUserMainAPI(PremiumOrderPaidAndActive premiumOrderPaidAndActive) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("validUntillUTC", premiumOrderPaidAndActive.getValidTillUTC().toString());
        headers.put("userID", premiumOrderPaidAndActive.getEmailProfileID());

        requisitionGenericSharedChecks("/internal-premium-actions/activate-premium",
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

        List<PremiumOrderPendind> premiumPendind = premiumPendingRepo.findAll();

        premiumPendind.forEach(x -> {
            MerchantOrdersThroughElementsDTO merchantOrderElements =
                    requisitionGenericMP("/merchant_orders/search?external_reference=" + x.getOrderID().toString(),
                            HttpMethod.GET, null,
                            new ParameterizedTypeReference<MerchantOrdersThroughElementsDTO>() {
                            }, null);

            MerchantOrderDTO merchantOrderDTO = merchantOrderElements.elements().stream().findFirst().orElse(null);

            if (merchantOrderDTO.payments().stream().findFirst().orElse(null).status().equals("approved")) {

                movePendingpremiumToPaidAndActivatePremium(x, merchantOrderDTO);
            }
        });
    }


    @Scheduled(fixedDelay = 21600000)
    @Transactional
    public void takingOutExpiredPremium() {
        List<PremiumOrderPaidAndActive> premiumPaidRepo = premiumPaidAndActiveRepo.findAll();

        premiumPaidRepo.forEach(x -> {
            if (x.getValidTillUTC().isBefore(LocalDateTime.now(ZoneOffset.UTC))) {
                x.setStatus("PAIDANDEXPIRED");
                var premiumPaidAndExpiredSaved = premiumPaidAndExpiredRepo.save(new PremiumOrderPaidAndExpired(x));
                premiumPaidAndActiveRepo.deleteById(premiumPaidAndExpiredSaved.getOrderID());

                // Deactivate User premium in main API
//                requisitionGenericSharedChecks("/internal-premium-actions/deactivate-premium",
//                        HttpMethod.DELETE, null,
//                        new ParameterizedTypeReference<Object>() {
//                        }, null);
            }
        });
    }
}
