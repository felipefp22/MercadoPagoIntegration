package com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.MerchantOrders.MerchantOrdersDTOs;

import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.MerchantOrders.MerchantOrder;

import java.util.List;

public record MerchantOrdersThroughElementsDTO(
        List<MerchantOrder> elements
) {
}
