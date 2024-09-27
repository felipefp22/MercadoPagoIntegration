package com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.MerchantOrders.MerchantOrdersDTOs;

import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.Preference.PreferenceDTOS.ItemsDTO;

import java.util.List;

public record MerchantOrderDTO(
        Long id,
        String status,
        String external_reference,
        String preference_id,
        List<PaymentsDTO>payments,
        List<Object> shipment,
        List<Object> payouts,
        CollectorDTO collector,
        String marketplace,
        String notification_url,
        String date_created,
        String last_updated,
        String sponsor_id,
        Double shipping_cost,
        Double total_amount,
        String site_id,
        Double paid_amount,
        Double refunded_amount,
        CollectorDTO payer,
        List<ItemsDTO> items,
        Boolean cancelled,
        String additional_info,
        String application_id,
        Boolean is_test,
        String order_status
) {
}
