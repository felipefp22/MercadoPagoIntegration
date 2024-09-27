package com.SharedCheksMercadoPagoIntegration.WebHook;

public record WebHookDTO(
        String action,
        String application_id,
        Object data,
        String date_created,
        String id,
        Boolean live_mode,
        String status,
        String type,
        Long user_id,
        Integer version
) {
}
