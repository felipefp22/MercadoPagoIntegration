package com.SharedCheksMercadoPagoIntegration.Infra.auth;

import com.SharedCheksMercadoPagoIntegration.Entities.AtuhUser.AuthUserLogin;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenServiceOur {

    @Value("${oauth.jwt.secret}")
    private String secretKey;


    // <>--------------- Methods ---------------<>
    public String generateToken(AuthUserLogin authUserLogin){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secretKey);

            String token = JWT.create()
                    .withIssuer("prev")
                    .withSubject(authUserLogin.getEmail())
                    .withClaim("username", authUserLogin.getUsername())
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);

            return token;

        }catch (JWTCreationException exception){
            throw new RuntimeException("Error while generating token", exception);
        }
    }

    public String validateTokenGetID(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secretKey);

            return JWT.require(algorithm)
                    .withIssuer("prev")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTCreationException exception){
            return "";
        }
    }

    public String validateTokenGetUsername(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secretKey);

            return JWT.require(algorithm)
                    .withIssuer("prev")
                    .build()
                    .verify(token)
                    .getClaim("username").asString();
        } catch (JWTCreationException exception){
            return "";
        }
    }

    private Instant genExpirationDate(){
        return LocalDateTime.now()
                .plusHours(10)
                .toInstant(ZoneOffset.UTC);
    }

    public Instant genRefreshTokenExpirationDate(){
        return LocalDateTime.now()
                .plusDays(30)
                .toInstant(ZoneOffset.UTC);
    }
}
