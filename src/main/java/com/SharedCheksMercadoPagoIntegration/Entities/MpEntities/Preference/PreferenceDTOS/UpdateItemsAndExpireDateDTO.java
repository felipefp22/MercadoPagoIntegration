package com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.Preference.PreferenceDTOS;

import java.util.List;

public record UpdateItemsAndExpireDateDTO (
        String expiration_date_to,
        List<ItemsDTO> items

){
}
