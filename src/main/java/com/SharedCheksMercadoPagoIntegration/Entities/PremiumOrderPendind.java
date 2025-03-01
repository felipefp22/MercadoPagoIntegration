package com.SharedCheksMercadoPagoIntegration.Entities;

import com.SharedCheksMercadoPagoIntegration.Entities.Enums.KindOfPremium;
import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.Preference.PreferenceDTOS.ItemsDTO;
import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.Preference.PreferenceDTOS.PayerDTO;
import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.Preference.PreferenceInfos;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Entity
public class PremiumOrderPendind {

    @Id
    private UUID orderID;

    private String emailProfileID;
    private String status;
    private LocalDateTime createdAtUTC;
    private LocalDateTime updatedExpirationAtUTC;

    private Double value;
    private KindOfPremium kindOfPremium;
    private String ticketUsed;

    private String mercadoPagoID;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PreferenceInfos preferenceInfos;

    // <>---------------- Constructors ----------------<>
    public PremiumOrderPendind() {
    }

    public PremiumOrderPendind(PayerDTO payerDTO, ItemsDTO itemsDTO) {
        this.orderID = UUID.randomUUID();
        this.emailProfileID = payerDTO.email();
        this.status = "";
        this.createdAtUTC = LocalDateTime.now(ZoneOffset.UTC);
        this.updatedExpirationAtUTC = this.createdAtUTC;
        this.value = itemsDTO.unit_price();
        this.kindOfPremium = KindOfPremium.valueOf(itemsDTO.title());
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

    public LocalDateTime getUpdatedExpirationAtUTC() {
        return updatedExpirationAtUTC;
    }
    public void setUpdatedExpirationAtUTC(LocalDateTime updatedExpirationAtUTC) {
        this.updatedExpirationAtUTC = updatedExpirationAtUTC;
    }

    public Double getValue() {
        return value;
    }

    public KindOfPremium getKindOfPremium() {
        return kindOfPremium;
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

