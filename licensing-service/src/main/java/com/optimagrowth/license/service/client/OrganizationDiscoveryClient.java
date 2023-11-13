package com.optimagrowth.license.service.client;

import com.optimagrowth.license.model.Organization;
import com.optimagrowth.license.utils.UserContextInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class OrganizationDiscoveryClient {

    @Autowired
    private DiscoveryClient discoveryClient;

    public Organization getOrganization(String organizationId) {
        RestTemplate restTemplate = getConfiguredRestTemplate();

        List<ServiceInstance> instances = discoveryClient.getInstances("gateway-server");

        if (instances.isEmpty()) return null;
        String serviceUri = String.format("%s/organization/v1/organization/%s", instances.get(0).getUri().toString(), organizationId);

        ResponseEntity<Organization> restExchange =
                restTemplate.exchange(
                        serviceUri,
                        HttpMethod.GET,
                        null, Organization.class, organizationId);

        return restExchange.getBody();
    }

    private RestTemplate getConfiguredRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        List<ClientHttpRequestInterceptor> outgoingRequestsInterceptors =
                restTemplate.getInterceptors();

        // To pass outbound request's data (correlation ID, Keycloak JWT, etc.)
        // for tracing to the calling service
        outgoingRequestsInterceptors.add(new UserContextInterceptor());

        restTemplate.setInterceptors(outgoingRequestsInterceptors);

        return restTemplate;
    }
}
