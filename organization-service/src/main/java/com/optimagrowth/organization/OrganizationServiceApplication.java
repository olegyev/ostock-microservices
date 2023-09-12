package com.optimagrowth.organization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@SpringBootApplication
@RefreshScope // POST - /actuator/refresh on a target service
public class OrganizationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrganizationServiceApplication.class, args);
    }

    // Internationalization
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(Locale.US);
        return localeResolver;
    }

    // Internationalization
    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();

        // When a message is not found, this option returns the message code instead of an error.
        // E.g.:
        // - only a message code will be returned: 'organization.create.message'
        // - instead of an error: "No message found under code 'organization.create.message' for locale 'es'"
        messageSource.setUseCodeAsDefaultMessage(true);

        // See file 'messages.properties'
        messageSource.setBasenames("messages");

        return messageSource;
    }
}
