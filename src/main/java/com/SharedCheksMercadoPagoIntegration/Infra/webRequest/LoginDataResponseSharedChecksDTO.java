package com.SharedCheksMercadoPagoIntegration.Infra.webRequest;

public record LoginDataResponseSharedChecksDTO(
        String access_token,
        String token_type
) {
}
