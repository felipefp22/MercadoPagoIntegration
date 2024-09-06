package com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.MerchantOrders.MerchantOrdersDTOs;

public record PaymentsDTO(
        String id,
        Double transaction_amount,
        Double total_paid_amount,
        Double shipping_cost,
        String status,
        String date_approved,
        String date_created,
        String date_modified

) {
}
