package com.SharedCheksMercadoPagoIntegration.Entities.Enums;

public enum KindOfSubscription {

    PREMIUMUSER("PremiumUser");

    private final String kindOfSubscription;

    KindOfSubscription(String kindOfSubscription) {
        this.kindOfSubscription = kindOfSubscription;
    }

    public String getKindOfSubscription() {
        return kindOfSubscription;
    }
}
