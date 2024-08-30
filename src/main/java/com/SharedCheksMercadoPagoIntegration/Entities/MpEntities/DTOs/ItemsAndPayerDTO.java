package com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.DTOs;

import java.util.List;

public record ItemsAndPayerDTO (
        PayerDTO payer,
        List<ItemsDTO> items
){
}
