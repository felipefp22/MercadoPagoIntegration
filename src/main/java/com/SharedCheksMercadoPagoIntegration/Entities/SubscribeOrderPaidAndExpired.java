package com.SharedCheksMercadoPagoIntegration.Entities;

import com.SharedCheksMercadoPagoIntegration.Entities.Enums.KindOfSubscription;
import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.MerchantOrders.MerchantOrder;
import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.Preference.PreferenceDTOS.ItemsDTO;
import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.Preference.PreferenceDTOS.PayerDTO;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class SubscribeOrderPaidAndExpired {

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

    @OneToOne(cascade = CascadeType.ALL)
    private MerchantOrder merchantOrder;

    // <>---------------- Constructors ----------------<>
    public SubscribeOrderPaidAndExpired() {
    }
    public SubscribeOrderPaidAndExpired(SubscribeOrderPaidAndActive subscribeOrderPaidAndActive){
        this.orderID = subscribeOrderPaidAndActive.getOrderID();
        this.emailProfileID = subscribeOrderPaidAndActive.getEmailProfileID();
        this.status = subscribeOrderPaidAndActive.getStatus();
        this.createdAt = subscribeOrderPaidAndActive.getCreatedAt();
        this.paidAt = LocalDateTime.now();
        this.validTill = subscribeOrderPaidAndActive.getValidTill();
        this.value = subscribeOrderPaidAndActive.getValue();
        this.kindOfSubscription = subscribeOrderPaidAndActive.getKindOfSubscription();
        this.mercadoPagoID = subscribeOrderPaidAndActive.getMercadoPagoID();
        this.merchantOrder = subscribeOrderPaidAndActive.getMerchantOrder();
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

