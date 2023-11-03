package com.optimagrowth.license.service.client;

import com.optimagrowth.license.model.Organization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrganizationRestClientFactory {

    private static final Logger logger = LoggerFactory.getLogger(OrganizationRestClientFactory.class);

    @Autowired
    private OrganizationFeignClient organizationFeignClient;

    @Autowired
    private OrganizationRestTemplateClient organizationRestTemplateClient;

    @Autowired
    private OrganizationDiscoveryClient organizationDiscoveryClient;

    public Organization retrieveOrganizationInfo(String organizationId, String clientType) {
        return switch (clientType) {
            case "discovery" -> {
                logger.debug("I am using the discovery client");
                yield organizationDiscoveryClient.getOrganization(organizationId);
            }
            case "rest" -> {
                logger.debug("I am using the rest client");
                yield organizationRestTemplateClient.getOrganization(organizationId);
            }
            case "feign" -> {
                logger.debug("I am using the feign client");
                yield organizationFeignClient.getOrganization(organizationId);
            }
            default -> organizationRestTemplateClient.getOrganization(organizationId);
        };
    }
}
