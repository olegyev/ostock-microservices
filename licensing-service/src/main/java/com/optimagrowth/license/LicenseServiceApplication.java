package com.optimagrowth.license;

import com.optimagrowth.license.config.RedisConfig;
import com.optimagrowth.license.utils.UserContextInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.List;
import java.util.Locale;

@SpringBootApplication
@RefreshScope // POST - /actuator/refresh on a target service - updates configs at run-time
@EnableDiscoveryClient
@EnableFeignClients
public class LicenseServiceApplication {

    @Autowired
    private RedisConfig redisConfig;

    public static void main(String[] args) {
        SpringApplication.run(LicenseServiceApplication.class, args);
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
        // - only a message code will be returned: 'license.create.message'
        // - instead of an error: "No message found under code 'license.create.message' for locale 'es'"
        messageSource.setUseCodeAsDefaultMessage(true);

        // See file 'messages.properties'
        messageSource.setBasenames("messages");

        return messageSource;
    }

    /**
     * @see com.optimagrowth.license.service.client.OrganizationRestTemplateClient
     */
    @LoadBalanced
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        RestTemplate restTemplate = builder.build();

        List<ClientHttpRequestInterceptor> outgoingRequestsInterceptors =
                restTemplate.getInterceptors();

        // To pass outbound request's data (correlation ID, Keycloak JWT, etc.)
        // for tracing to the calling service
        outgoingRequestsInterceptors.add(new UserContextInterceptor());

        restTemplate.setInterceptors(outgoingRequestsInterceptors);

        return restTemplate;
    }

    /**
     * Redis
     * See <a href="https://docs.spring.io/spring-data/data-redis/docs/current/reference/html/#redis.repositories.usage">docs</a>
     * See <a href="https://www.baeldung.com/spring-data-redis-tutorial">tutorial</a>
     * <p>
     * Lettuce client is used, instead of Jedis, because it is default for spring-boot-starter-data-redis
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        String redisHostName = redisConfig.getHostName();
        int redisPort = Integer.parseInt(redisConfig.getPort());

        RedisStandaloneConfiguration redisStandaloneConfiguration
                = new RedisStandaloneConfiguration(redisHostName, redisPort);

        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
}
