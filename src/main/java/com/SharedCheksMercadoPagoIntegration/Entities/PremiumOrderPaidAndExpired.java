package com.SharedCheksMercadoPagoIntegration.Entities;

import com.SharedCheksMercadoPagoIntegration.Entities.Enums.KindOfPremium;
import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.MerchantOrders.MerchantOrder;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class PremiumOrderPaidAndExpired {

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
    public PremiumOrderPaidAndExpired() {
    }
    public PremiumOrderPaidAndExpired(PremiumOrderPaidAndActive premiumOrderPaidAndActive){
        this.orderID = premiumOrderPaidAndActive.getOrderID();
        this.emailProfileID = premiumOrderPaidAndActive.getEmailProfileID();
        this.status = premiumOrderPaidAndActive.getStatus();
        this.createdAtUTC = premiumOrderPaidAndActive.getCreatedAtUTC();
        this.getUpdatedExpirationAtUTC = premiumOrderPaidAndActive.getGetUpdatedExpirationAtUTC();
        this.paidAtUTC = premiumOrderPaidAndActive.getPaidAtUTC();
        this.validTillUTC = premiumOrderPaidAndActive.getValidTillUTC();
        this.value = premiumOrderPaidAndActive.getValue();
        this.kindOfPremium = premiumOrderPaidAndActive.getKindOfSubscription();
        this.mercadoPagoID = premiumOrderPaidAndActive.getMercadoPagoID();
        this.merchantOrder = premiumOrderPaidAndActive.getMerchantOrder();
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

