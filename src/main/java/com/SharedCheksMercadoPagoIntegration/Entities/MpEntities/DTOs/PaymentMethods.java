package com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.DTOs;

import java.util.List;

public record PaymentMethods(
        List<ExcludedPaymentMethods> excluded_payment_methods

) {
}
