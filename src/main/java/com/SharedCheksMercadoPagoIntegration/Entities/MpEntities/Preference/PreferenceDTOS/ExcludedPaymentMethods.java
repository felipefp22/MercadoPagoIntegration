package com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.Preference.PreferenceDTOS;

public record ExcludedPaymentMethods(
        String id
) {
    public ExcludedPaymentMethods(String id) {
        this.id = id;
    }
}
