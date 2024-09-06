package com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.Preference.PreferenceDTOS;

import java.util.List;

public record ItemsAndPayerDTO (
        PayerDTO payer,
        List<ItemsDTO> items
){
}
