package com.SharedCheksMercadoPagoIntegration.Entities;

import com.SharedCheksMercadoPagoIntegration.Entities.Enums.KindOfSubscription;
import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.Preference.PreferenceDTOS.ItemsDTO;
import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.Preference.PreferenceDTOS.PayerDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class SubscribeOrderPendind {

    @Id
    private UUID orderID;

    private String emailProfileID;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime paidAt;
    private LocalDateTime validTill;

    private Double value;
    private KindOfSubscription kindOfSubscription;
    private String ticketUsed;

    private String mercadoPagoID;

    // <>---------------- Constructors ----------------<>
    public SubscribeOrderPendind() {
    }
    public SubscribeOrderPendind(PayerDTO payerDTO, ItemsDTO itemsDTO) {
        this.orderID = UUID.randomUUID();
        this.emailProfileID = payerDTO.email();
        this.status = "";
        this.createdAt = LocalDateTime.now();
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }
    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }

    public LocalDateTime getValidTill() {
        return validTill;
    }
    public void setValidTill(LocalDateTime validTill) {
        this.validTill = validTill;
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

