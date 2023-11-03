package com.optimagrowth.license.service.client;

import com.optimagrowth.license.model.Organization;
import com.optimagrowth.license.repository.redis.OrganizationRedisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrganizationRedisClient {

    private static final Logger logger = LoggerFactory.getLogger(OrganizationRedisClient.class);

    @Autowired
    private OrganizationRedisRepository organizationRedisRepository;

    /* To increase resiliency, we never let the entire call fail if we cannot communicate
      with the Redis server. Instead, we log the exception and let the call
      through to the organization service. In this particular case, caching is meant
      to help improve performance, and the absence of the caching server
      should not impact the success of the call. */

    public Organization checkRedisCache(String organizationId) {
        Organization cachedOrganization = null;

        try {
            cachedOrganization = organizationRedisRepository.findById(organizationId).orElse(null);
        } catch (Exception ex) {
            logger.error("Error occurred while trying to retrieve Organization {} from Redis cache. Exception {}", organizationId, ex.getMessage());
        }

        return cachedOrganization;
    }

    public void cacheOrganization(Organization organization) {
        try {
            organizationRedisRepository.save(organization);
        } catch (Exception ex) {
            logger.error("Unable to cache Organization {} in Redis. Exception {}", organization, ex.getMessage());
        }
    }

    public void deleteFromCache(String organizationId) {
        try {
            organizationRedisRepository.deleteById(organizationId);
        } catch (Exception ex) {
            logger.error("Unable to delete Organization {} from Redis cache. Exception {}", organizationId, ex.getMessage());
        }
    }
}
