package com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.MerchantOrders;

import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.MerchantOrders.MerchantOrdersDTOs.CollectorDTO;
import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.MerchantOrders.MerchantOrdersDTOs.MerchantOrderDTO;
import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.MerchantOrders.MerchantOrdersDTOs.PaymentsDTO;
import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.Preference.PreferenceDTOS.ItemsDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Entity
public class MerchantOrder {

    @Id
    private Long id;
    private String status;
    private String external_reference;
    private String preference_id;
    private List<Long> paymentsids;
    private String marketplace;
    private String notification_url;
    private String date_created;
    private String last_updated;
    private String sponsor_id;
    private Double shipping_cost;
    private Double total_amount;
    private String site_id;
    private Double paid_amount;
    private Double refunded_amount;
    private Boolean cancelled;
    private String additional_info;
    private String application_id;
    private Boolean is_test;
    private String order_status;


    // <>---------------- Constructors ----------------<>
    public MerchantOrder() {
    }
    public MerchantOrder(MerchantOrderDTO merchantOrderDTO){
        this.id = merchantOrderDTO.id();
        this.status = merchantOrderDTO.status();
        this.external_reference = merchantOrderDTO.external_referen();
        this.preference_id = merchantOrderDTO.preference_id();
        this.paymentsids = merchantOrderDTO.payments().stream().map(PaymentsDTO::id).collect(Collectors.toList());
        this.marketplace = merchantOrderDTO.marketplace();
        this.notification_url = merchantOrderDTO.notification_url();
        this.date_created = merchantOrderDTO.date_created();
        this.last_updated = merchantOrderDTO.last_updated();
        this.sponsor_id = merchantOrderDTO.sponsor_id();
        this.shipping_cost = merchantOrderDTO.shipping_cost();
        this.total_amount = merchantOrderDTO.total_amount();
        this.site_id = merchantOrderDTO.site_id();
        this.paid_amount = merchantOrderDTO.paid_amount();
        this.refunded_amount = merchantOrderDTO.refunded_amount();
        this.cancelled = merchantOrderDTO.cancelled();
        this.additional_info = merchantOrderDTO.additional_info();
        this.application_id = merchantOrderDTO.application_id();
        this.is_test = merchantOrderDTO.is_test();
        this.order_status = merchantOrderDTO.order_status();
    }


    // <>---------------- GETTERS and SETTERS ----------------<>


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getExternal_reference() {
        return external_reference;
    }
    public void setExternal_reference(String external_reference) {
        this.external_reference = external_reference;
    }

    public String getPreference_id() {
        return preference_id;
    }
    public void setPreference_id(String preference_id) {
        this.preference_id = preference_id;
    }

    public List<Long> getPaymentsids() {
        return paymentsids;
    }
    public void setPaymentsids(List<PaymentsDTO> payments) {
        this.paymentsids = payments.stream().map(PaymentsDTO::id).collect(Collectors.toList());
    }

    public String getMarketplace() {
        return marketplace;
    }
    public void setMarketplace(String marketplace) {
        this.marketplace = marketplace;
    }

    public String getNotification_url() {
        return notification_url;
    }
    public void setNotification_url(String notification_url) {
        this.notification_url = notification_url;
    }

    public String getDate_created() {
        return date_created;
    }
    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getLast_updated() {
        return last_updated;
    }
    public void setLast_updated(String last_updated) {
        this.last_updated = last_updated;
    }

    public String getSponsor_id() {
        return sponsor_id;
    }
    public void setSponsor_id(String sponsor_id) {
        this.sponsor_id = sponsor_id;
    }

    public Double getShipping_cost() {
        return shipping_cost;
    }
    public void setShipping_cost(Double shipping_cost) {
        this.shipping_cost = shipping_cost;
    }

    public Double getTotal_amount() {
        return total_amount;
    }
    public void setTotal_amount(Double total_amount) {
        this.total_amount = total_amount;
    }

    public String getSite_id() {
        return site_id;
    }
    public void setSite_id(String site_id) {
        this.site_id = site_id;
    }

    public Double getPaid_amount() {
        return paid_amount;
    }
    public void setPaid_amount(Double paid_amount) {
        this.paid_amount = paid_amount;
    }

    public Double getRefunded_amount() {
        return refunded_amount;
    }
    public void setRefunded_amount(Double refunded_amount) {
        this.refunded_amount = refunded_amount;
    }

    public Boolean getCancelled() {
        return cancelled;
    }
    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
    }

    public String getAdditional_info() {
        return additional_info;
    }
    public void setAdditional_info(String additional_info) {
        this.additional_info = additional_info;
    }

    public String getApplication_id() {
        return application_id;
    }
    public void setApplication_id(String application_id) {
        this.application_id = application_id;
    }

    public Boolean getIs_test() {
        return is_test;
    }
    public void setIs_test(Boolean is_test) {
        this.is_test = is_test;
    }

    public String getOrder_status() {
        return order_status;
    }
    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

}
