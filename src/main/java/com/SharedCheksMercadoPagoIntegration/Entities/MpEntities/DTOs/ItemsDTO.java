package com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.DTOs;

public record ItemsDTO(
    String description,
    Integer quantity,
    Integer unit_price,
    String title
) {
}
