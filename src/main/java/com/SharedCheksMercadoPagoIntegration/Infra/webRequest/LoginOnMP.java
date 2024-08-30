package com.SharedCheksMercadoPagoIntegration.Infra.webRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LoginOnMP {

    @Value("${mp.client.id}")
    private String client_id;

    @Value("${mp.client.secret}")
    private String client_secret;

    private String grant_type = "client_credentials";

    @Value("${mp.url}")
    private String url;

    // <>---|Getters|---<>

    public String getClient_id() {
        return client_id;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public String getGrant_type() {
        return grant_type;
    }

    public String getUrl() {
        return url;
    }
}
