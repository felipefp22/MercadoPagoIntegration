package com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.Preference;


import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.Preference.PreferenceDTOS.*;

import java.util.List;

public record PreferenceRetunDTO(
        String addtional_info,
        String auto_return,
        BackUrlsDTO back_urls,
        Boolean binary_mode,
        Long client_id,
        Long collector_id,
        Object coupon_code,
        Object coupon_labels,
        String date_created,
        String date_of_expiration,
        String expiration_date_from,
        String expiration_date_to,
        Boolean expires,
        String external_reference,
        String id,
        String init_point,
        Object internal_metadata,
        List<ItemsDTO> items,
        Long marketplace_fee,
        Object metadata,
        String notification_url,
        String operation_type,
        PayerDTO payer,
        PaymentMethods payment_methods,
        Object processing_modes,
        Object product_id,
        RedirectUrlsDTO redirect_urls,
        String sandbox_init_point,
        String site_id,
        Object shipments,
        Object total_amount,
        String last_updated,
        String statement_descriptor
) {


}
