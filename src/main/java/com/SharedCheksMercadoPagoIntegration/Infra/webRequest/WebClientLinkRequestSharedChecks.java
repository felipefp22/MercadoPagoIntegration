package com.SharedCheksMercadoPagoIntegration.Infra.webRequest;


import io.netty.resolver.DefaultAddressResolverGroup;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.netty.http.client.HttpClient;

import java.util.Map;

@Component
public class WebClientLinkRequestSharedChecks {
    private static LoginOnSharedChecks loginOnSharedChecks;
    private static String tokenOnSharedChecks;
    private static WebClient webClient;

    public WebClientLinkRequestSharedChecks(LoginOnSharedChecks loginOnSharedChecks) {
        this.loginOnSharedChecks = loginOnSharedChecks;
        webClient = WebClient.builder()
                .baseUrl(loginOnSharedChecks.getUrl())
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create()
                        .resolver(DefaultAddressResolverGroup.INSTANCE)))
                .build();
    }

    // <>---|Methods|-----------------------------------------------<>

    public static <T> T requisitionGenericSharedChecks(String uri, HttpMethod httpMethod, Object requestBody,
                                                       ParameterizedTypeReference<T> responseType, Map<String, String> headers) {

        return retryRequestSharedChecks(uri, httpMethod, requestBody, responseType,
                headers, 5);
    }
    public static <T> T retryRequestSharedChecks(String uri, HttpMethod httpMethod, Object requestBody,
                                                 ParameterizedTypeReference<T> responseType, Map<String, String> headers, int remainingRetries) {

        try {
            return webClient
                    .method(httpMethod)
                    .uri(uri)
                    .headers(httpHeaders -> {
                        if (headers != null) {
                            headers.forEach(httpHeaders::add);
                        }
                        httpHeaders.add("Authorization", tokenOnSharedChecks);
                    })
                    .body(requestBody != null ? BodyInserters.fromValue(requestBody) : null)
                    .retrieve()
                    .bodyToMono(responseType)
                    .block();

        } catch (WebClientException e) {
            if (e instanceof WebClientResponseException && remainingRetries > 0) {
                WebClientResponseException ex = (WebClientResponseException) e;
                if (ex.getStatusCode().equals(HttpStatus.UNAUTHORIZED) || ex.getStatusCode().equals(HttpStatus.FORBIDDEN)
                        || ex.getStatusCode().equals(HttpStatus.valueOf(503))) {
                    // Call your login method here
                    loginOnSharedChecks();
                    // Retry the request
                    return retryRequestSharedChecks(uri, httpMethod, requestBody, responseType, headers,remainingRetries - 1);
                }
            }
            throw e;
        }
    }

    public static void loginOnSharedChecks() {
        var requisitionPath = ("/oauth/token");

        var responseOfWitsis =
                requisitionGenericSharedChecks(requisitionPath, HttpMethod.POST, loginOnSharedChecks,new ParameterizedTypeReference<LoginDataResponseDTO>() {},null);

        tokenOnSharedChecks = (responseOfWitsis.token_type() + " " + responseOfWitsis.access_token());
    }
}
