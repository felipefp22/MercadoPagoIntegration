package com.SharedCheksMercadoPagoIntegration.Entities;

import com.SharedCheksMercadoPagoIntegration.Entities.Enums.KindOfSubscription;
import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.Preference.PreferenceDTOS.PreferenceRetunDTO;
import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.Preference.PreferenceInfos;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class SubscribeOrderCancelled {

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
    public SubscribeOrderCancelled() {
    }

    public SubscribeOrderCancelled(SubscribeOrderPendind subscribeOrderPendind){
        this.orderID = subscribeOrderPendind.getOrderID();
        this.emailProfileID = subscribeOrderPendind.getEmailProfileID();
        this.status = "";
        this.createdAtUTC = subscribeOrderPendind.getCreatedAtUTC();
        this.value = subscribeOrderPendind.getValue();
        this.kindOfSubscription = subscribeOrderPendind.getKindOfSubscription();
        this.mercadoPagoID = subscribeOrderPendind.getMercadoPagoID();
        this.preferenceInfos = subscribeOrderPendind.getPreferenceInfos();
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
}

