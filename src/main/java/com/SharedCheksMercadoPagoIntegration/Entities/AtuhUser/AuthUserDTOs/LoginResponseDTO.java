package com.SharedCheksMercadoPagoIntegration.Entities.AtuhUser.AuthUserDTOs;

import java.util.UUID;

public record LoginResponseDTO(
        String token_type,
        String access_token
) {
    public LoginResponseDTO (String access_token){
        this("Bearer", access_token);
    }

}
