package com.SharedCheksMercadoPagoIntegration.Infra.webRequest;

public record LoginDataResponseDTO(
        String access_token,
        String token_type
) {
}
