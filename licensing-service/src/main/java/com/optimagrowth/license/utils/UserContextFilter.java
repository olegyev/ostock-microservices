package com.optimagrowth.license.utils;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Inbound requests
 */

@Component
public class UserContextFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(UserContextFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        UserContextHolder.getContext()
                .setCorrelationId(
                        httpServletRequest.getHeader(UserContext.CORRELATION_ID));

        UserContextHolder.getContext().setUserId(
                httpServletRequest.getHeader(UserContext.USER_ID));

        UserContextHolder.getContext().setAuthToken(
                httpServletRequest.getHeader(UserContext.AUTH_TOKEN));

        UserContextHolder.getContext().setOrganizationId(
                httpServletRequest.getHeader(UserContext.ORGANIZATION_ID));

        UserContextHolder.getContext().setJwtKeycloak(
                httpServletRequest.getHeader(UserContext.JWT_KEYCLOAK));

        logger.debug("INBOUND REQUEST: UserContextFilter Correlation ID: {}, JWT: {}",
                UserContextHolder.getContext().getCorrelationId(),
                UserContextHolder.getContext().getJwtKeycloak());

        filterChain.doFilter(httpServletRequest, servletResponse);
    }
}
