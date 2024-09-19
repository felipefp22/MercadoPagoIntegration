package com.SharedCheksMercadoPagoIntegration.Servicies;

import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.Preference.Preference;
import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.Preference.PreferenceDTOS.*;
import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.Preference.PreferenceDTOS.PreferenceRetunDTO;
import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.Preference.PreferenceInfos;
import com.SharedCheksMercadoPagoIntegration.Entities.SubscribeOrderCancelled;
import com.SharedCheksMercadoPagoIntegration.Entities.SubscribeOrderPendind;
import com.SharedCheksMercadoPagoIntegration.Repositories.SubscriptionCancelledRepo;
import com.SharedCheksMercadoPagoIntegration.Repositories.SubscriptionPendingRepo;
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
    private final SubscriptionPendingRepo subscriptionPendingRepo;
    private final SubscriptionCancelledRepo subscriptionCancelledRepo;

    public OrdersService(SubscriptionPendingRepo subscriptionPendingRepo, SubscriptionCancelledRepo subscriptionCancelledRepo) {
        this.subscriptionPendingRepo = subscriptionPendingRepo;
        this.subscriptionCancelledRepo = subscriptionCancelledRepo;
    }

    // <>-------------- Methods --------------<>
    public PreferenceRetunDTO createOrder(PayerDTO payerDTO, ItemsDTO itemsDTO) {
        try {
            SubscribeOrderPendind subscribeOrderPendindFound =
                    subscriptionPendingRepo.findByEmailProfileID(payerDTO.email()).stream().findFirst().orElse(null);

            if (subscribeOrderPendindFound != null) {

                return updateExpirationOrder(subscribeOrderPendindFound, itemsDTO);
            } else {
                SubscribeOrderPendind subscribeOrderPendind = new SubscribeOrderPendind(payerDTO, itemsDTO);

                List<ExcludedPaymentMethods> excludedPaymentMethods =
                        List.of("bolbradesco", "pec").stream().map(ExcludedPaymentMethods::new).toList();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

                Preference preference = new Preference(
                        //true,
                        new BackUrlsDTO("www.google.com.br", "www.google.com.br", "www.google.com.br"),
                        subscribeOrderPendind.getOrderID().toString(),
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

                subscribeOrderPendind.setMercadoPagoID(returnOfMP.id());
                subscribeOrderPendind.setPreferenceInfos(new PreferenceInfos(returnOfMP));

                //precisa revisar isso aqui
                subscribeOrderPendind.setStatus("CREATED");
                subscriptionPendingRepo.save(subscribeOrderPendind);

                return returnOfMP;
            }
        } catch (WebClientException e) {
            System.out.println(e.getMessage().toString());
        }
        return null;
    }

    // <>-------- Aux Public Methods --------<>
    @Transactional
    public void cancelAndExpireNoFurtherOrder(SubscribeOrderPendind subscribeOrderPendindFound) {
        // First need to expire o MP for user can't pay wrong
        expireOrderOnMP(subscribeOrderPendindFound);

        subscribeOrderPendindFound.setStatus("CANCELLED");
        SubscribeOrderCancelled subscribeOrderCancelled = new SubscribeOrderCancelled(subscribeOrderPendindFound);
        SubscribeOrderCancelled subscribeOrderCancelledSaved = subscriptionCancelledRepo.save(subscribeOrderCancelled);
        subscriptionPendingRepo.deleteById(subscribeOrderCancelledSaved.getOrderID());
    }

    // <>-------- Aux Private Methods --------<>
    private PreferenceRetunDTO updateExpirationOrder(SubscribeOrderPendind subscribeOrderPendindFound,
                                                     ItemsDTO itemsDTO) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        PreferenceRetunDTO retrivedPreferenceFromMP =
                requisitionGenericMP("/checkout/preferences/" + subscribeOrderPendindFound.getMercadoPagoID(),
                        HttpMethod.GET, null,
                        new ParameterizedTypeReference<PreferenceRetunDTO>() {
                        },
                        null);

        if (subscribeOrderPendindFound.getKindOfSubscription().getItemsDTO() == itemsDTO) {

            subscribeOrderPendindFound.setUpdatedExpirationAtUTC(LocalDateTime.now(ZoneOffset.UTC));

            UpdateExpireDateDTO bodyToUpdateOnlyExpireDate =
                    new UpdateExpireDateDTO(
                            ZonedDateTime.of(LocalDateTime.now(ZoneOffset.UTC).plusDays(1), ZoneOffset.UTC).format(formatter));

            // Updating on MP
            PreferenceRetunDTO returnOfMP =
                    requisitionGenericMP("/checkout/preferences/" + subscribeOrderPendindFound.getMercadoPagoID(),
                            HttpMethod.PUT,
                            bodyToUpdateOnlyExpireDate,
                            new ParameterizedTypeReference<PreferenceRetunDTO>() {
                            },
                            null);

            subscriptionPendingRepo.save(subscribeOrderPendindFound);

            return returnOfMP;

        } else {
            throw new RuntimeException("The order already exists with different values, want cancel and create a new one?");
        }
    }

    private void expireOrderOnMP(SubscribeOrderPendind subscribeOrderPendindFound) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        UpdateExpireDateDTO bodyToExpireOrder =
                new UpdateExpireDateDTO(
                        ZonedDateTime.of(LocalDateTime.now(ZoneOffset.UTC).minusMinutes(5), ZoneOffset.UTC).format(formatter));

        requisitionGenericMP("/checkout/preferences/" + subscribeOrderPendindFound.getMercadoPagoID(),
                HttpMethod.PUT,
                bodyToExpireOrder,
                new ParameterizedTypeReference<PreferenceRetunDTO>() {
                },
                null);
    }

    // <>-------------- Routines --------------<>
    @Scheduled(fixedDelay = 5000)
    public void moveOldOrdersNotPaidToCancelledRepo() {
        List<SubscribeOrderPendind> subscriptionsPendind = subscriptionPendingRepo.findAll();

        subscriptionsPendind.forEach(x -> {
            if (x.getUpdatedExpirationAtUTC().plusDays(1).isBefore(LocalDateTime.now(ZoneOffset.UTC))) {

                cancelAndExpireNoFurtherOrder(x);
            }
        });
    }
}
