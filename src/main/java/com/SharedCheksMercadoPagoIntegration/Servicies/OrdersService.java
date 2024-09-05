package com.SharedCheksMercadoPagoIntegration.Servicies;

import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.DTOs.*;
import com.SharedCheksMercadoPagoIntegration.Entities.SubscribeOrder;
import com.SharedCheksMercadoPagoIntegration.Repositories.SubscriptionPendentRepo;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientException;

import java.util.List;
import java.util.UUID;

import static com.SharedCheksMercadoPagoIntegration.Infra.webRequest.WebClientLinkRequest.requisitionGeneric;

@Service
public class OrdersService {
    private final SubscriptionPendentRepo subscriptionPendentRepo;

    public OrdersService(SubscriptionPendentRepo subscriptionPendentRepo) {
        this.subscriptionPendentRepo = subscriptionPendentRepo;
    }

    // <>-------------- Methods --------------<>
    public Object createOrder(PayerDTO payerDTO, ItemsDTO itemsDTO) {
        try {
            SubscribeOrder subscribeOrder = new SubscribeOrder(payerDTO, itemsDTO);
            List<ExcludedPaymentMethods> excludedPaymentMethods =
                    List.of("bolbradesco", "pec").stream().map(ExcludedPaymentMethods::new).toList();

            PreferenceDTO preferenceDTO = new PreferenceDTO(
                    UUID.randomUUID().toString(),
                    false,
                    null,
                    List.of(itemsDTO),
                    payerDTO,
                    new PaymentMethods(excludedPaymentMethods)
            );

            Object returnOfMP = requisitionGeneric("/checkout/preferences", HttpMethod.POST, preferenceDTO,
                    new ParameterizedTypeReference<Object>() {
                    }, null);

            //precisa revisar isso aqui
            subscribeOrder.setStatus("CREATED");
            subscriptionPendentRepo.save(subscribeOrder);

            return returnOfMP;

        } catch (WebClientException e) {
            System.out.println(e.getMessage().toString());
        }
        return null;
    }
}
