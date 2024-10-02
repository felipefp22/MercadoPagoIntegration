package com.SharedCheksMercadoPagoIntegration.Entities.AtuhUser.AuthUserDTOs;

import jakarta.validation.constraints.*;

public record RegisterAuthUserDTO(
        @NotBlank @NotEmpty String username,
        @NotBlank @NotEmpty @Size(min = 6, message = "Password must at least 6 digits") String password
) {

}
