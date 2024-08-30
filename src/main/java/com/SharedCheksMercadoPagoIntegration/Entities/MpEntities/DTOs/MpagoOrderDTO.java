package com.SharedCheksMercadoPagoIntegration.Entities.MpEntities.DTOs;

import jakarta.persistence.Entity;

import java.util.List;

@Entity
public record MpagoOrderDTO(
        String external_reference,
        List<ItemsDTO> items,
        //String notification_url,
        PayerDTO payer
) {


}
