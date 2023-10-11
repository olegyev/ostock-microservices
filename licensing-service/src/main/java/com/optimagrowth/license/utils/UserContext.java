package com.optimagrowth.license.utils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ToString
@Component
public class UserContext {

    public static final String CORRELATION_ID = "tmx-correlation-id";
    public static final String AUTH_TOKEN = "tmx-auth-token";
    public static final String USER_ID = "tmx-user-id";
    public static final String ORGANIZATION_ID = "tmx-organization-id";
    public static final String JWT_KEYCLOAK = "Authorization";

    private String correlationId = "";
    private String authToken = "";
    private String userId = "";
    private String organizationId = "";
    private String jwtKeycloak = "";
}
