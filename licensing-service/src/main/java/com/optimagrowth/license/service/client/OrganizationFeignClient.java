package com.optimagrowth.license.service.client;

import com.optimagrowth.license.config.FeignClientConfig;
import com.optimagrowth.license.model.Organization;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * For configuration details,
 * see https://www.javacodemonk.com/feign-requestinterceptor-in-spring-boot-cbe5d967
 * */

@FeignClient(value = "organization-service", configuration = {FeignClientConfig.class})
public interface OrganizationFeignClient {

    @GetMapping(value = "/v1/organization/{organizationId}", consumes = "application/json")
    Organization getOrganization(@PathVariable("organizationId") String organizationId);
}
