package com.SharedCheksMercadoPagoIntegration.Entities;

import com.SharedCheksMercadoPagoIntegration.Entities.Enums.KindOfPremium;
import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.MerchantOrders.MerchantOrder;
import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.MerchantOrders.MerchantOrdersDTOs.MerchantOrderDTO;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class PremiumOrderPaidAndActive {

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
    public PremiumOrderPaidAndActive() {
    }
    public PremiumOrderPaidAndActive(PremiumOrderPendind premiumOrderPendind){
        this.orderID = premiumOrderPendind.getOrderID();
        this.emailProfileID = premiumOrderPendind.getEmailProfileID();
        this.status = premiumOrderPendind.getStatus();
        this.createdAtUTC = premiumOrderPendind.getCreatedAtUTC();
        this.getUpdatedExpirationAtUTC = premiumOrderPendind.getUpdatedExpirationAtUTC();
        this.value = premiumOrderPendind.getValue();
        this.kindOfPremium = premiumOrderPendind.getKindOfPremium();
        this.mercadoPagoID = premiumOrderPendind.getMercadoPagoID();
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

    public MerchantOrder getMerchantOrder() {
        return merchantOrder;
    }
    public void setMerchantOrderFromDTO(MerchantOrderDTO merchantOrderDTO) {
        this.merchantOrder = new MerchantOrder(merchantOrderDTO);
    }
}

