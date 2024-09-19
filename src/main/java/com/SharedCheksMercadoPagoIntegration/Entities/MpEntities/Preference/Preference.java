package com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.Preference;


import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.Preference.PreferenceDTOS.BackUrlsDTO;
import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.Preference.PreferenceDTOS.ItemsDTO;
import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.Preference.PreferenceDTOS.PayerDTO;
import com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.Preference.PreferenceDTOS.PaymentMethodsDTO;

import java.util.List;

public record Preference(
        //Boolean auto_return,
        BackUrlsDTO back_urls,
        String external_reference,
        Boolean expires,
        String expiration_date_to,
        List<ItemsDTO> items,
        String notification_url,
        PayerDTO payer,
        PaymentMethodsDTO payment_methods,
        Integer installments
) {


}
