package com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.MerchantOrders.MerchantOrdersDTOs;

public record PaymentsDTO(
        Long id,
        Double transaction_amount,
        Double total_paid_amount,
        Double shipping_cost,
        String status,
        String status_detail,
        String operation_type,
        String date_approved,
        String date_created,
        String last_modified,
        String amount_refunded

) {
}
