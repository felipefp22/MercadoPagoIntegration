package com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.MerchantOrders;

import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.MerchantOrders.MerchantOrdersDTOs.CollectorDTO;
import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.MerchantOrders.MerchantOrdersDTOs.PaymentsDTO;
import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.Preference.PreferenceDTOS.ItemsDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MerchantOrder{

    private Integer id;
    private String status;
    private String external_reference;
    private String preference_id;
    private List<PaymentsDTO> payments;
    private List<Object> shipments;
    private List<Object> payouts;
    private CollectorDTO collector;
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
    private CollectorDTO payer;
    private List<ItemsDTO> items;
    private Boolean cancelled;
    private String additional_info;
    private String application_id;
    private Boolean is_test;
    private String order_status;

    // <>---------------- Constructors ----------------<>



    // <>---------------- GETTERS and SETTERS ----------------<>


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public List<PaymentsDTO> getPayments() {
        return payments;
    }

    public void setPayments(List<PaymentsDTO> payments) {
        this.payments = payments;
    }

    public List<Object> getShipments() {
        return shipments;
    }

    public void setShipments(List<Object> shipments) {
        this.shipments = shipments;
    }

    public List<Object> getPayouts() {
        return payouts;
    }

    public void setPayouts(List<Object> payouts) {
        this.payouts = payouts;
    }

    public CollectorDTO getCollector() {
        return collector;
    }

    public void setCollector(CollectorDTO collector) {
        this.collector = collector;
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

    public CollectorDTO getPayer() {
        return payer;
    }

    public void setPayer(CollectorDTO payer) {
        this.payer = payer;
    }

    public List<ItemsDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemsDTO> items) {
        this.items = items;
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
