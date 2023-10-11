package com.optimagrowth.license.config;

import com.optimagrowth.license.utils.UserContext;
import com.optimagrowth.license.utils.UserContextHolder;
import feign.RequestInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;

public class FeignClientConfig {

    private static final Logger logger = LoggerFactory.getLogger(FeignClientConfig.class);

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header(UserContext.CORRELATION_ID,
                    UserContextHolder.getContext().
                            getCorrelationId());

            requestTemplate.header(UserContext.JWT_KEYCLOAK,
                    UserContextHolder.getContext().
                            getJwtKeycloak());

            logger.debug("OUTBOUND REQUEST - FEIGN CLIENT: UserContextFilter Correlation ID: {}, JWT: {}",
                    UserContextHolder.getContext().getCorrelationId(),
                    UserContextHolder.getContext().getJwtKeycloak());
        };
    }
}
