package com.SharedCheksMercadoPagoIntegration.Entities.AtuhUser.AuthUserDTOs;

import com.SharedCheksMercadoPagoIntegration.Entities.AtuhUser.AuthUserLogin;

public record AuthUserDTO(
        String username
) {
    public AuthUserDTO(AuthUserLogin authUserLogin) {
        this(authUserLogin.getUsername());
    }

}
