package com.SharedCheksMercadoPagoIntegration.Entities.Enums;

import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.Preference.PreferenceDTOS.ItemsDTO;

public enum KindOfSubscription {
    PREMIUMTESTE("PremiumTeste", 30, new ItemsDTO("Assinatura 0 days", "BRL", 1, 0.01, "PREMIUMTESTE")),
    PREMIUM30("Premium30", 30, new ItemsDTO("Assinatura 30 days", "BRL", 1, 9.99, "PREMIUM30")),
    PREMIUM90("Premium90", 90, new ItemsDTO("Assinatura 90 days", "BRL", 1, 26.99, "PREMIUM90")),
    PREMIUM365("PremiumUser365", 365, new ItemsDTO("Assinatura 365 days", "BRL", 1, 99.99, "PREMIUM365"));

    private final String kindOfSubscription;
    private final int days;
    private final ItemsDTO itemsDTO;

    KindOfSubscription(String kindOfSubscription, int days, ItemsDTO itemsDTO) {
        this.kindOfSubscription = kindOfSubscription;
        this.days = days;
        this.itemsDTO = itemsDTO;
    }

    public String getKindOfSubscription() {
        return kindOfSubscription;
    }

    public int getDays() {
        return days;
    }

    public ItemsDTO getItemsDTO() {
        return itemsDTO;
    }
}
