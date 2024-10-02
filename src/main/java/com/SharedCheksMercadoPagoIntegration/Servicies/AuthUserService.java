package com.SharedCheksMercadoPagoIntegration.Servicies;

import com.SharedCheksMercadoPagoIntegration.Entities.AtuhUser.AuthUserDTOs.AuthenticationDTO;
import com.SharedCheksMercadoPagoIntegration.Entities.AtuhUser.AuthUserDTOs.LoginResponseDTO;
import com.SharedCheksMercadoPagoIntegration.Entities.AtuhUser.AuthUserDTOs.RegisterAuthUserDTO;
import com.SharedCheksMercadoPagoIntegration.Entities.AtuhUser.AuthUserLogin;
import com.SharedCheksMercadoPagoIntegration.Infra.auth.TokenServiceOur;
import com.SharedCheksMercadoPagoIntegration.Repositories.AuthUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthUserService {
    private final AuthUserRepository authUserRepository;
    private final TokenServiceOur tokenServiceOur;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthUserService(AuthUserRepository authUserRepository, TokenServiceOur tokenServiceOur) {
        this.authUserRepository = authUserRepository;
        this.tokenServiceOur = tokenServiceOur;
    }

    // <>--------------- Methodos ---------------<>

    @Transactional
    public LoginResponseDTO login(AuthenticationDTO authenticationDTO) {
        AuthUserLogin authUserLogin =
                authUserRepository.findByUsernameIgnoreCase(authenticationDTO.username())
                        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!new BCryptPasswordEncoder().matches(authenticationDTO.password(), authUserLogin.getPassword())) {
            throw new RuntimeException("Login information incorrect");
        }

        return new LoginResponseDTO(tokenServiceOur.generateToken(authUserLogin));
    }

    @Transactional
    public AuthUserLogin createUsuario(RegisterAuthUserDTO registerAuthUserDTO) {
        verifyIfUserExists(registerAuthUserDTO);
        if (authUserRepository.existsById(registerAuthUserDTO.username()))
            throw new RuntimeException("Usuário já cadastrado");

        AuthUserLogin authUserLogin = new AuthUserLogin(registerAuthUserDTO);

        return authUserRepository.save(authUserLogin);
    }


    // <>--------------- Methodos Auxiliares ---------------<>

    public void verifyIfUserExists(RegisterAuthUserDTO registerAuthUserDTO) {
        authUserRepository.findByUsernameIgnoreCase(registerAuthUserDTO.username()).ifPresent(usuario -> {
            throw new RuntimeException("Username is not available");
        });
    }
}
