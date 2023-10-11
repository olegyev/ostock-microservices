package com.optimagrowth.license.config;

import com.optimagrowth.license.utils.UserContext;
import com.optimagrowth.license.utils.UserContextHolder;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header(UserContext.JWT_KEYCLOAK,
                    UserContextHolder.getContext().
                            getJwtKeycloak());
        };
    }
}
