package com.SharedCheksMercadoPagoIntegration.Entities;

import com.SharedCheksMercadoPagoIntegration.Entities.Enums.KindOfPremium;
import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.MerchantOrders.MerchantOrder;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class SubscribeOrderPaidAndExpired {

    @Id
    private UUID orderID;


    private String emailProfileID;
    private String status;
    private LocalDateTime createdAtUTC;
    private LocalDateTime getUpdatedExpirationAtUTC;
    private LocalDateTime paidAtUTC;
    private LocalDateTime validTillUTC;

    private Double value;
    private KindOfPremium kindOfPremium;
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
        this.createdAtUTC = subscribeOrderPaidAndActive.getCreatedAtUTC();
        this.getUpdatedExpirationAtUTC = subscribeOrderPaidAndActive.getGetUpdatedExpirationAtUTC();
        this.paidAtUTC = subscribeOrderPaidAndActive.getPaidAtUTC();
        this.validTillUTC = subscribeOrderPaidAndActive.getValidTillUTC();
        this.value = subscribeOrderPaidAndActive.getValue();
        this.kindOfPremium = subscribeOrderPaidAndActive.getKindOfSubscription();
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

    public LocalDateTime getCreatedAtUTC() {
        return createdAtUTC;
    }

    public LocalDateTime getGetUpdatedExpirationAtUTC() {
        return getUpdatedExpirationAtUTC;
    }
    public void setGetUpdatedExpirationAtUTC(LocalDateTime getUpdatedExpirationAtUTC) {
        this.getUpdatedExpirationAtUTC = getUpdatedExpirationAtUTC;
    }

    public LocalDateTime getPaidAtUTC() {
        return paidAtUTC;
    }
    public void setPaidAtUTC(LocalDateTime paidAtUTC) {
        this.paidAtUTC = paidAtUTC;
    }

    public LocalDateTime getValidTillUTC() {
        return validTillUTC;
    }
    public void setValidTillUTC(LocalDateTime validTillUTC) {
        this.validTillUTC = validTillUTC;
    }

    public Double getValue() {
        return value;
    }

    public KindOfPremium getKindOfSubscription() {
        return kindOfPremium;
    }

    public String getMercadoPagoID() {
        return mercadoPagoID;
    }
    public void setMercadoPagoID(String mercadoPagoID) {
        this.mercadoPagoID = mercadoPagoID;
    }
}

