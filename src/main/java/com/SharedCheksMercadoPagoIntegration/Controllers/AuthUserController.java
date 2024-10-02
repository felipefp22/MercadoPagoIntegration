package com.SharedCheksMercadoPagoIntegration.Controllers;

import com.SharedCheksMercadoPagoIntegration.Entities.AtuhUser.AuthUserDTOs.AuthenticationDTO;
import com.SharedCheksMercadoPagoIntegration.Entities.AtuhUser.AuthUserDTOs.LoginResponseDTO;
import com.SharedCheksMercadoPagoIntegration.Entities.AtuhUser.AuthUserDTOs.RegisterAuthUserDTO;
import com.SharedCheksMercadoPagoIntegration.Infra.auth.RetriveAuthInfosService;
import com.SharedCheksMercadoPagoIntegration.Servicies.AuthUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthUserController {
    private final AuthUserService authUserService;
    private final RetriveAuthInfosService retriveAuthInfosService;

    public AuthUserController(AuthUserService authUserService, RetriveAuthInfosService retriveAuthInfosService) {
        this.authUserService = authUserService;
        this.retriveAuthInfosService = retriveAuthInfosService;
    }


    // <>--------------- Methodos ---------------<>

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody AuthenticationDTO authenticationDTO){

        LoginResponseDTO loginResponseDTO = authUserService.login(authenticationDTO);

        if(loginResponseDTO != null){
            return ResponseEntity.ok(loginResponseDTO);
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterAuthUserDTO registerAuthUserDTO) {
        authUserService.createUsuario(registerAuthUserDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
