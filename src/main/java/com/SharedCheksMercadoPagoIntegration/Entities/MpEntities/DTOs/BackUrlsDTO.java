package com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.DTOs;

public record BackUrlsDTO(
        String success,
        String pending,
        String failure
) {
}
