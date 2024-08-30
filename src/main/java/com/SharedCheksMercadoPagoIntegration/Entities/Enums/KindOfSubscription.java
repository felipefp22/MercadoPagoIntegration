package com.SharedCheksMercadoPagoIntegration.Entities.Enums;

public enum KindOfSubscription {

    PREMIUMUSER30("Premium30", 30),
    PREMIUMUSER90("Premium90", 90),
    PREMIUMUSER365("PremiumUser365", 365);

    private final String kindOfSubscription;
    private final int dias;

    KindOfSubscription(String kindOfSubscription, int dias) {
        this.kindOfSubscription = kindOfSubscription;
        this.dias = dias;
    }

    public String getKindOfSubscription() {
        return kindOfSubscription;
    }
}
