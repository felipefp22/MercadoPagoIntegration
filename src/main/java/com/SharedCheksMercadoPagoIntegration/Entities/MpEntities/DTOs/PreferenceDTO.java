package com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.DTOs;


import java.util.List;

public record PreferenceDTO(
        BackUrlsDTO back_urls,
        String external_reference,
        Boolean expires,
        String expiration_date_to,
        List<ItemsDTO> items,
        String notification_url,
        PayerDTO payer,
        PaymentMethods payment_methods
) {


}
