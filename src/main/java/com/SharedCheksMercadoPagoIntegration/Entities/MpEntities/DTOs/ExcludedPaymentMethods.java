package com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.DTOs;

public record ExcludedPaymentMethods(
        String id
) {
    public ExcludedPaymentMethods(String id) {
        this.id = id;
    }
}
