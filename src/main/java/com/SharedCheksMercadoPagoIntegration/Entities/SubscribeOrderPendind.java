package com.SharedCheksMercadoPagoIntegration.Entities;

import com.SharedCheksMercadoPagoIntegration.Entities.Enums.KindOfSubscription;
import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.Preference.PreferenceDTOS.ItemsDTO;
import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.Preference.PreferenceDTOS.PayerDTO;
import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.Preference.PreferenceDTOS.PreferenceRetunDTO;
import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.Preference.PreferenceInfos;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Entity
public class SubscribeOrderPendind {

    @Id
    private UUID orderID;

    private String emailProfileID;
    private String status;
    private LocalDateTime createdAtUTC;

    private Double value;
    private KindOfSubscription kindOfSubscription;
    private String ticketUsed;

    private String mercadoPagoID;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PreferenceInfos preferenceInfos;

    // <>---------------- Constructors ----------------<>
    public SubscribeOrderPendind() {
    }

    public SubscribeOrderPendind(PayerDTO payerDTO, ItemsDTO itemsDTO) {
        this.orderID = UUID.randomUUID();
        this.emailProfileID = payerDTO.email();
        this.status = "";
        this.createdAtUTC = LocalDateTime.now(ZoneOffset.UTC);
        this.value = itemsDTO.unit_price();
        this.kindOfSubscription = KindOfSubscription.valueOf(itemsDTO.title());
    }


    // <>---------------- GETTERS and SETTERS ----------------<>


    public UUID getOrderID() {
        return orderID;
    }

    public String getEmailProfileID() {
        return emailProfileID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAtUTC() {
        return createdAtUTC;
    }

    public Double getValue() {
        return value;
    }

    public KindOfSubscription getKindOfSubscription() {
        return kindOfSubscription;
    }

    public String getMercadoPagoID() {
        return mercadoPagoID;
    }

    public void setMercadoPagoID(String mercadoPagoID) {
        this.mercadoPagoID = mercadoPagoID;
    }

    public PreferenceInfos getPreferenceInfos() {
        return preferenceInfos;
    }

    public void setPreferenceInfos(PreferenceInfos preferenceInfos) {
        this.preferenceInfos = preferenceInfos;
    }
}

