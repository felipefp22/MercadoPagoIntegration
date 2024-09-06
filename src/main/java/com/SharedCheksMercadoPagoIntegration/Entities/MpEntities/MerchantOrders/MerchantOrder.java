package com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.MerchantOrders;

import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.MerchantOrders.MerchantOrdersDTOs.PaymentsDTO;

import java.util.List;

public record MerchantOrder(
        Integer id,
        String external_reference,
        String preference_id,
        List<PaymentsDTO> payments

) {


}
