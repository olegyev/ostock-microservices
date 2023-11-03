package com.optimagrowth.license.service.client;

import com.optimagrowth.license.model.KeycloakTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class KeycloakClient {

    private static final Logger logger = LoggerFactory.getLogger(KeycloakClient.class);

    public KeycloakTokenResponse getToken() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("username", "user.admin");
        body.add("password", "1234567890");
        body.add("grant_type", "password");
        body.add("client_id", "Ostock");
        body.add("client_secret", "**********");
        body.add("scope", "openid");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        String keycloakUrl = "http://keycloak:8080/realms/MyAppRealm/protocol/openid-connect/token";

        ResponseEntity<KeycloakTokenResponse> responseEntity = restTemplate.exchange(
                keycloakUrl,
                HttpMethod.POST,
                requestEntity,
                KeycloakTokenResponse.class
        );

        KeycloakTokenResponse responseToken = responseEntity.getBody();

        logger.debug("Keycloak token received: {}", responseToken);

        return responseToken;
    }
}
