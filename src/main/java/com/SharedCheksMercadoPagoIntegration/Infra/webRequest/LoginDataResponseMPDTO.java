package com.SharedCheksMercadoPagoIntegration.Infra.webRequest;

public record LoginDataResponseMPDTO(
        String access_token,
        String token_type
) {
}
