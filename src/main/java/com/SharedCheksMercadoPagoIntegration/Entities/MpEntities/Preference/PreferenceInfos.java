package com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.Preference;


import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.Preference.PreferenceDTOS.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class PreferenceInfos {
    private String addtional_info;
    private String auto_return;
    //private BackUrlsDTO back_urls;
    private Boolean binary_mode;
    private Long client_id;
    private Long collector_id;
    private String coupon_code; //object
    private String coupon_labels; //object
    private String date_created;
    private String date_of_expiration;
    private String expiration_date_from;
    private String expiration_date_to;
    private Boolean expires;
    private String external_reference;

    @Id
    private String id;
    private String init_point;
    private String internal_metadata; //object
    //private List<ItemsDTO> items;
    private Long marketplace_fee;
    private String metadata; //object
    private String notification_url;
    private String operation_type;
    //private PayerDTO payer;
    //private PaymentMethodsDTO payment_methods;
    private String processing_modes; //object
    private String product_id;  //object
    //private BackUrlsDTO redirect_urls;
    private String sandbox_init_point;
    private String site_id;
    private String shipments;  //object
    private String total_amount;  //object
    private String last_updated;
    private String statement_descriptor;

    // <>---------------- Constructors ----------------<>
    public PreferenceInfos() {
    }
    public PreferenceInfos(PreferenceRetunDTO preferenceRetunDTO){
        this.addtional_info = preferenceRetunDTO.addtional_info();
        this.auto_return = preferenceRetunDTO.auto_return();
        //this.back_urls = preferenceRetunDTO.back_urls();
        this.binary_mode = preferenceRetunDTO.binary_mode();
        this.client_id = preferenceRetunDTO.client_id();
        this.collector_id = preferenceRetunDTO.collector_id();
        this.coupon_code = preferenceRetunDTO.coupon_code() != null ? preferenceRetunDTO.coupon_code().toString():null;
        this.coupon_labels = preferenceRetunDTO.coupon_labels()!=null? preferenceRetunDTO.coupon_labels().toString():null;
        this.date_created = preferenceRetunDTO.date_created();
        this.date_of_expiration = preferenceRetunDTO.date_of_expiration();
        this.expiration_date_from = preferenceRetunDTO.expiration_date_from();
        this.expiration_date_to = preferenceRetunDTO.expiration_date_to();
        this.expires = preferenceRetunDTO.expires();
        this.external_reference = preferenceRetunDTO.external_reference();
        this.id = preferenceRetunDTO.id();
        this.init_point = preferenceRetunDTO.init_point();
        this.internal_metadata = preferenceRetunDTO.internal_metadata()!=null ? preferenceRetunDTO.internal_metadata().toString():null;
        //this.items = preferenceRetunDTO.items();
        this.marketplace_fee = preferenceRetunDTO.marketplace_fee();
        this.metadata = preferenceRetunDTO.metadata()!=null ? preferenceRetunDTO.metadata().toString():null;
        this.notification_url = preferenceRetunDTO.notification_url();
        this.operation_type = preferenceRetunDTO.operation_type();
        //this.payer = preferenceRetunDTO.payer();
        //this.payment_methods = preferenceRetunDTO.payment_methods();
        this.processing_modes = preferenceRetunDTO.processing_modes()!=null ? preferenceRetunDTO.processing_modes().toString():null;
        this.product_id = preferenceRetunDTO.product_id()!=null ? preferenceRetunDTO.product_id().toString():null;
        //this.redirect_urls = preferenceRetunDTO.redirect_urls();
        this.sandbox_init_point = preferenceRetunDTO.sandbox_init_point();
        this.site_id = preferenceRetunDTO.site_id();
        this.shipments = preferenceRetunDTO.shipments()!=null ? preferenceRetunDTO.shipments().toString():null;
        this.total_amount = preferenceRetunDTO.total_amount()!=null ? preferenceRetunDTO.total_amount().toString():null;
        this.last_updated = preferenceRetunDTO.last_updated();
        this.statement_descriptor = preferenceRetunDTO.statement_descriptor();
    }

    // <>---------------- Getters and Setters ----------------<>


    public String getAddtional_info() {
        return addtional_info;
    }

    public String getAuto_return() {
        return auto_return;
    }

//    public BackUrlsDTO getBack_urls() {
//        return back_urls;
//    }

    public Boolean getBinary_mode() {
        return binary_mode;
    }

    public Long getClient_id() {
        return client_id;
    }

    public Long getCollector_id() {
        return collector_id;
    }

    public Object getCoupon_code() {
        return coupon_code;
    }

    public Object getCoupon_labels() {
        return coupon_labels;
    }

    public String getDate_created() {
        return date_created;
    }

    public String getDate_of_expiration() {
        return date_of_expiration;
    }

    public String getExpiration_date_from() {
        return expiration_date_from;
    }

    public String getExpiration_date_to() {
        return expiration_date_to;
    }

    public Boolean getExpires() {
        return expires;
    }

    public String getExternal_reference() {
        return external_reference;
    }

    public String getId() {
        return id;
    }

    public String getInit_point() {
        return init_point;
    }

    public Object getInternal_metadata() {
        return internal_metadata;
    }

//    public List<ItemsDTO> getItems() {
//        return items;
//    }

    public Long getMarketplace_fee() {
        return marketplace_fee;
    }

    public Object getMetadata() {
        return metadata;
    }

    public String getNotification_url() {
        return notification_url;
    }

    public String getOperation_type() {
        return operation_type;
    }

//    public PayerDTO getPayer() {
//        return payer;
//    }

//    public PaymentMethodsDTO getPayment_methods() {
//        return payment_methods;
//    }

    public Object getProcessing_modes() {
        return processing_modes;
    }

    public Object getProduct_id() {
        return product_id;
    }

//    public BackUrlsDTO getRedirect_urls() {
//        return redirect_urls;
//    }

    public String getSandbox_init_point() {
        return sandbox_init_point;
    }

    public String getSite_id() {
        return site_id;
    }

    public Object getShipments() {
        return shipments;
    }

    public Object getTotal_amount() {
        return total_amount;
    }

    public String getLast_updated() {
        return last_updated;
    }

    public String getStatement_descriptor() {
        return statement_descriptor;
    }
}
