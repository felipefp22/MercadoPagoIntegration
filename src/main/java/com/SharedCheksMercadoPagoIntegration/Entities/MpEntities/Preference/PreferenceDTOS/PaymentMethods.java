package com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.Preference.PreferenceDTOS;

import java.util.List;

public record PaymentMethods(
        List<ExcludedPaymentMethods> excluded_payment_methods

) {
}
