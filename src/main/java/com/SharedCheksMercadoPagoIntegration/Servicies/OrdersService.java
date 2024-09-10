package com.SharedCheksMercadoPagoIntegration.Servicies;

import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.Preference.Preference;
import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.Preference.PreferenceDTOS.*;
import com.SharedCheksMercadoPagoIntegration.Entities.SubscribeOrderPendind;
import com.SharedCheksMercadoPagoIntegration.Repositories.SubscriptionPendentRepo;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
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
    private final SubscriptionPendentRepo subscriptionPendentRepo;

    public OrdersService(SubscriptionPendentRepo subscriptionPendentRepo) {
        this.subscriptionPendentRepo = subscriptionPendentRepo;
    }

    // <>-------------- Methods --------------<>
    public Object createOrder(PayerDTO payerDTO, ItemsDTO itemsDTO) {
        try {
            SubscribeOrderPendind subscribeOrderPendind = new SubscribeOrderPendind(payerDTO, itemsDTO);
            List<ExcludedPaymentMethods> excludedPaymentMethods =
                    List.of("bolbradesco", "pec").stream().map(ExcludedPaymentMethods::new).toList();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

            Preference preference = new Preference(
                    new BackUrlsDTO("www.google.com.br", "www.google.com.br", "www.google.com.br"),
                    subscribeOrderPendind.getOrderID().toString(),
                    true,
                    ZonedDateTime.of(LocalDateTime.now(ZoneOffset.UTC).plusDays(1), ZoneOffset.UTC).format(formatter),
                    List.of(itemsDTO),
                    "https://0e44-2603-8000-6d01-e38a-cc9e-d968-a165-3c3a.ngrok-free.app/webhook-receives/mp-payments",
                    payerDTO,
                    new PaymentMethods(excludedPaymentMethods),
                    1
            );

            Object returnOfMP = requisitionGenericMP("/checkout/preferences", HttpMethod.POST, preference,
                    new ParameterizedTypeReference<Object>() {
                    }, null);

            //precisa revisar isso aqui
            subscribeOrderPendind.setStatus("CREATED");
            subscriptionPendentRepo.save(subscribeOrderPendind);

            return returnOfMP;

        } catch (WebClientException e) {
            System.out.println(e.getMessage().toString());
        }
        return null;
    }
}
