package com.optimagrowth.license.events.handler;

import com.optimagrowth.license.events.model.ActionEnum;
import com.optimagrowth.license.events.model.OrganizationChangeModel;
import com.optimagrowth.license.model.KeycloakTokenResponse;
import com.optimagrowth.license.model.Organization;
import com.optimagrowth.license.service.client.KeycloakClient;
import com.optimagrowth.license.service.client.OrganizationRedisClient;
import com.optimagrowth.license.service.client.OrganizationRestClientFactory;
import com.optimagrowth.license.utils.UserContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrganizationChangeEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(OrganizationChangeEventHandler.class);

    @Autowired
    private OrganizationRestClientFactory organizationRestClientFactory;

    @Autowired
    private OrganizationRedisClient organizationRedisClient;

    @Autowired
    KeycloakClient keycloakClient;

    public void processOrganizationChangedEvent(OrganizationChangeModel event) {
        logger.debug("From Kafka before processing: {}", event);

        Organization organizationRetrieved;
        String restClientType = "feign";

        ActionEnum action = ActionEnum.valueOf(event.getAction());
        String organizationId = event.getOrganizationId();

        switch (action) {
            case CREATED:
            case UPDATED:
                KeycloakTokenResponse keycloakToken = keycloakClient.getToken();
                String jwtKeycloak = keycloakToken.getAccessToken();
                UserContextHolder.getContext().setJwtKeycloak("Bearer " + jwtKeycloak);

                organizationRetrieved = organizationRestClientFactory.retrieveOrganizationInfo(organizationId, restClientType);
                organizationRedisClient.cacheOrganization(organizationRetrieved);
                logger.debug("Organization cached in Redis by Kafka event {}: {}", action.getValue(), organizationRetrieved);
                break;
            case DELETED:
                organizationRedisClient.deleteFromCache(organizationId);
                logger.debug("Organization removed from Redis cache by Kafka event {}: {}", action.getValue(), organizationId);
                break;
            default:
                logger.debug("No action specified by Kafka event: {}", organizationId);
        }
    }
}
