package com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.DTOs;


import java.time.LocalDateTime;
import java.util.List;

public record PreferenceDTO(
        String external_reference,
        Boolean expires,
        LocalDateTime expiration_date_to,
        List<ItemsDTO> items,
        //String notification_url,
        PayerDTO payer,
        PaymentMethods payment_methods
) {


}
