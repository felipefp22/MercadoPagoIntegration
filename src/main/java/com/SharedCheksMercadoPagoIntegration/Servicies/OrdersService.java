package com.SharedCheksMercadoPagoIntegration.Servicies;

import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.Preference.Preference;
import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.Preference.PreferenceDTOS.*;
import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.Preference.PreferenceDTOS.PreferenceRetunDTO;
import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.Preference.PreferenceInfos;
import com.SharedCheksMercadoPagoIntegration.Entities.PremiumOrderCancelled;
import com.SharedCheksMercadoPagoIntegration.Entities.PremiumOrderPendind;
import com.SharedCheksMercadoPagoIntegration.Repositories.PremiumCancelledRepo;
import com.SharedCheksMercadoPagoIntegration.Repositories.PremiumPendingRepo;
import jakarta.transaction.Transactional;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.SharedCheksMercadoPagoIntegration.Infra.webRequest.WebClientLinkRequestMP.requisitionGenericMP;

@Service
public class OrdersService {
    private final PremiumPendingRepo premiumPendingRepo;
    private final PremiumCancelledRepo premiumCancelledRepo;

    public OrdersService(PremiumPendingRepo premiumPendingRepo, PremiumCancelledRepo premiumCancelledRepo) {
        this.premiumPendingRepo = premiumPendingRepo;
        this.premiumCancelledRepo = premiumCancelledRepo;
    }

    // <>-------------- Methods --------------<>
    public PreferenceRetunDTO createOrder(PayerDTO payerDTO, ItemsDTO itemsDTO) {
        try {
            PremiumOrderPendind premiumOrderPendindFound =
                    premiumPendingRepo.findByEmailProfileID(payerDTO.email()).stream().findFirst().orElse(null);

            if (premiumOrderPendindFound != null) {

                return updateExpirationOrder(premiumOrderPendindFound, itemsDTO);
            } else {
                PremiumOrderPendind premiumOrderPendind = new PremiumOrderPendind(payerDTO, itemsDTO);

                List<ExcludedPaymentMethods> excludedPaymentMethods =
                        List.of("bolbradesco", "pec").stream().map(ExcludedPaymentMethods::new).toList();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

                Preference preference = new Preference(
                        //true,
                        new BackUrlsDTO("www.google.com.br", "www.google.com.br", "www.google.com.br"),
                        premiumOrderPendind.getOrderID().toString(),
                        true,
                        ZonedDateTime.of(LocalDateTime.now(ZoneOffset.UTC).plusDays(1), ZoneOffset.UTC).format(formatter),
                        List.of(itemsDTO),
                        "https://0e44-2603-8000-6d01-e38a-cc9e-d968-a165-3c3a.ngrok-free.app/webhook-receives/mp-payments",
                        payerDTO,
                        new PaymentMethodsDTO(excludedPaymentMethods),
                        1
                );

                PreferenceRetunDTO returnOfMP =
                        requisitionGenericMP("/checkout/preferences", HttpMethod.POST, preference,
                                new ParameterizedTypeReference<PreferenceRetunDTO>() {
                                },
                                null);

                premiumOrderPendind.setMercadoPagoID(returnOfMP.id());
                premiumOrderPendind.setPreferenceInfos(new PreferenceInfos(returnOfMP));

                //precisa revisar isso aqui
                premiumOrderPendind.setStatus("CREATED");
                premiumPendingRepo.save(premiumOrderPendind);

                return returnOfMP;
            }
        } catch (WebClientException e) {
            System.out.println(e.getMessage().toString());
        }
        return null;
    }

    // <>-------- Aux Public Methods --------<>
    @Transactional
    public void cancelAndExpireNoFurtherOrder(PremiumOrderPendind premiumOrderPendindFound) {
        // First need to expire o MP for user can't pay wrong
        expireOrderOnMP(premiumOrderPendindFound);

        premiumOrderPendindFound.setStatus("CANCELLED");
        PremiumOrderCancelled premiumOrderCancelled = new PremiumOrderCancelled(premiumOrderPendindFound);
        PremiumOrderCancelled premiumOrderCancelledSaved = premiumCancelledRepo.save(premiumOrderCancelled);
        premiumPendingRepo.deleteById(premiumOrderCancelledSaved.getOrderID());
    }

    // <>-------- Aux Private Methods --------<>
    private PreferenceRetunDTO updateExpirationOrder(PremiumOrderPendind premiumOrderPendindFound,
                                                     ItemsDTO itemsDTO) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        PreferenceRetunDTO retrivedPreferenceFromMP =
                requisitionGenericMP("/checkout/preferences/" + premiumOrderPendindFound.getMercadoPagoID(),
                        HttpMethod.GET, null,
                        new ParameterizedTypeReference<PreferenceRetunDTO>() {
                        },
                        null);

        if (premiumOrderPendindFound.getKindOfSubscription().getItemsDTO() == itemsDTO) {

            premiumOrderPendindFound.setUpdatedExpirationAtUTC(LocalDateTime.now(ZoneOffset.UTC));

            UpdateExpireDateDTO bodyToUpdateOnlyExpireDate =
                    new UpdateExpireDateDTO(
                            ZonedDateTime.of(LocalDateTime.now(ZoneOffset.UTC).plusDays(1), ZoneOffset.UTC).format(formatter));

            // Updating on MP
            PreferenceRetunDTO returnOfMP =
                    requisitionGenericMP("/checkout/preferences/" + premiumOrderPendindFound.getMercadoPagoID(),
                            HttpMethod.PUT,
                            bodyToUpdateOnlyExpireDate,
                            new ParameterizedTypeReference<PreferenceRetunDTO>() {
                            },
                            null);

            premiumPendingRepo.save(premiumOrderPendindFound);

            return returnOfMP;

        } else {
            throw new RuntimeException("The order already exists with different values, want cancel and create a new one?");
        }
    }

    private void expireOrderOnMP(PremiumOrderPendind premiumOrderPendindFound) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        UpdateExpireDateDTO bodyToExpireOrder =
                new UpdateExpireDateDTO(
                        ZonedDateTime.of(LocalDateTime.now(ZoneOffset.UTC).minusMinutes(5), ZoneOffset.UTC).format(formatter));

        requisitionGenericMP("/checkout/preferences/" + premiumOrderPendindFound.getMercadoPagoID(),
                HttpMethod.PUT,
                bodyToExpireOrder,
                new ParameterizedTypeReference<PreferenceRetunDTO>() {
                },
                null);
    }

    // <>-------------- Routines --------------<>
    @Scheduled(fixedDelay = 5000)
    public void moveOldOrdersNotPaidToCancelledRepo() {
        List<PremiumOrderPendind> subscriptionsPendind = premiumPendingRepo.findAll();

        subscriptionsPendind.forEach(x -> {
            if (x.getUpdatedExpirationAtUTC().plusDays(1).isBefore(LocalDateTime.now(ZoneOffset.UTC))) {

                cancelAndExpireNoFurtherOrder(x);
            }
        });
    }
}
