package com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.DTOs;

public record ItemsDTO(
    String description,
    String currency_id,
    Integer quantity,
    Double unit_price,
    String title
) {
}
