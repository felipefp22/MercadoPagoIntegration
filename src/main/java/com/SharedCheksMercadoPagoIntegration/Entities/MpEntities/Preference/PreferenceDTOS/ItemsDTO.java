package com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.Preference.PreferenceDTOS;

public record ItemsDTO(
    String description,
    String currency_id,
    Integer quantity,
    Double unit_price,
    String title
) {
}
