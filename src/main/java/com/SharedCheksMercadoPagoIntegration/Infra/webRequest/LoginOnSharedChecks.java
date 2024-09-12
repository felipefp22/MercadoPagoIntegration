package com.SharedCheksMercadoPagoIntegration.Infra.webRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LoginOnSharedChecks {

    @Value("${sharedchecks.email.login}")
    private String emailOrUsername;

    @Value("${sharedchecks.PASSWORD}")
    private String password;

    @Value("${sharedchecks.url}")
    private String url;

    // <>---|Getters|---<>


    public String getEmailOrUsername() {
        return emailOrUsername;
    }

    public String getPassword() {
        return password;
    }

    public String getUrl() {
        return url;
    }
}
