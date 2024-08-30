package com.SharedCheksMercadoPagoIntegration.Infra.webRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LoginOnMP {
//    @Value("${.username}")
//    private String username;
//
//    @Value("${.password}")
//    private String password;

    @Value("${mp.clientId}")
    private String client_id;

    @Value("${mp.clientSecret}")
    private String client_secret;

    @Value("${mp.url}")
    private String url;

    // <>---|Getters|---<>
//    public String getUsername() {
//        return username;
//    }
//
//    public String getPassword() {
//        return password;
//    }

    public String getClient_id() {
        return client_id;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public String getUrl() {
        return url;
    }
}
