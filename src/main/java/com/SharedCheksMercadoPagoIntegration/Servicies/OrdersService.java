package com.SharedCheksMercadoPagoIntegration.Servicies;

import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.DTOs.ItemsAndPayerDTO;
import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.DTOs.MpagoOrderDTO;
import org.springframework.stereotype.Service;

import static com.SharedCheksMercadoPagoIntegration.Infra.webRequest.WebClientLinkRequest.requisitionGeneric;

@Service
public class OrdersService {

    public Object createOrder(ItemsAndPayerDTO itemsAndPayerDTO) {
        MpagoOrderDTO mpagoOrderDTO = new MpagoOrderDTO(
                "external_reference",
                itemsAndPayerDTO.items(),
                itemsAndPayerDTO.payer()
        );

        Object returnOfMP = requisitionGeneric();

        return null;

    }
}
