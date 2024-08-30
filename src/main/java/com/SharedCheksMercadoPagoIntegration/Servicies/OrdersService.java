package com.SharedCheksMercadoPagoIntegration.Servicies;

import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.DTOs.ItemsAndPayerDTO;
import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.DTOs.ItemsDTO;
import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.DTOs.PayerDTO;
import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.DTOs.PreferenceDTO;
import com.SharedCheksMercadoPagoIntegration.Entities.SubscribeOrder;
import com.SharedCheksMercadoPagoIntegration.Repositories.SubscribeOrderRepo;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.SharedCheksMercadoPagoIntegration.Infra.webRequest.WebClientLinkRequest.requisitionGeneric;

@Service
public class OrdersService {
    private final SubscribeOrderRepo subscribeOrderRepo;

    public OrdersService(SubscribeOrderRepo subscribeOrderRepo) {
        this.subscribeOrderRepo = subscribeOrderRepo;
    }

    // <>-------------- Methods --------------<>
    public Object createOrder(PayerDTO payerDTO, ItemsDTO itemsDTO) {
        SubscribeOrder subscribeOrder = new SubscribeOrder(payerDTO, itemsDTO);

        PreferenceDTO preferenceDTO = new PreferenceDTO(
                UUID.randomUUID().toString(),
                false,
                null,
                List.of(itemsDTO),
                payerDTO
        );

        Object returnOfMP = requisitionGeneric("/checkout/preferences", HttpMethod.POST, preferenceDTO,
                new ParameterizedTypeReference<Object>() {}, null);

        //precisa revisar isso aqui
        subscribeOrder.setStatus("CREATED");
        subscribeOrderRepo.save(subscribeOrder);

        return returnOfMP;
    }
}
