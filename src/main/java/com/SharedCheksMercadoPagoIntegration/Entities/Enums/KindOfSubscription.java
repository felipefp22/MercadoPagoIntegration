package com.SharedCheksMercadoPagoIntegration.Entities.Enums;

import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.DTOs.ItemsDTO;

public enum KindOfSubscription {
    PREMIUMTESTE("Premium30", 30, new ItemsDTO("Assinatura 0 dias", "BRL", 1, 0.01, "PREMIUMTESTE")),
    PREMIUM30("Premium30", 30, new ItemsDTO("Assinatura 30 dias", "BRL", 1, 9.99, "PREMIUM30")),
    PREMIUM90("Premium90", 90, new ItemsDTO("Assinatura 90 dias", "BRL", 1, 26.99, "PREMIUM90")),
    PREMIUM365("PremiumUser365", 365, new ItemsDTO("Assinatura 365 dias", "BRL", 1, 99.99, "PREMIUM365"));

    private final String kindOfSubscription;
    private final int dias;
    private final ItemsDTO itemsDTO;

    KindOfSubscription(String kindOfSubscription, int dias, ItemsDTO itemsDTO) {
        this.kindOfSubscription = kindOfSubscription;
        this.dias = dias;
        this.itemsDTO = itemsDTO;
    }

    public String getKindOfSubscription() {
        return kindOfSubscription;
    }

    public int getDias() {
        return dias;
    }

    public ItemsDTO getItemsDTO() {
        return itemsDTO;
    }
}
