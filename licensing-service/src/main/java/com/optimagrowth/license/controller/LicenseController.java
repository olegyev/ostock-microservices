package com.optimagrowth.license.controller;

import com.optimagrowth.license.model.License;
import com.optimagrowth.license.service.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "v1/organization/{organizationId}/license")
public class LicenseController {

    @Autowired
    MessageSource messages;

    @Autowired
    LicenseService licenseService;

    @GetMapping(value = "/{licenseId}")
    public ResponseEntity<License> getLicense(
            @PathVariable("organizationId") String organizationId,
            @PathVariable("licenseId") String licenseId,
            @RequestHeader(value = "Accept-Language", required = false) Locale locale) {
        License license = licenseService.getLicense(licenseId, organizationId, locale);

        license.add(
                linkTo(methodOn(LicenseController.class).getLicense(organizationId, license.getLicenseId(), null)).withSelfRel(),
                linkTo(methodOn(LicenseController.class).createLicense(organizationId, license, null)).withRel("createLicense"),
                linkTo(methodOn(LicenseController.class).updateLicense(license, null)).withRel("updateLicense"),
                linkTo(methodOn(LicenseController.class).deleteLicense(organizationId, license.getLicenseId(), null)).withRel("deleteLicense")
        );

        return ResponseEntity.ok(license);
    }

    /**
     * @param clientType can be of the following values to invoke the organization service:
     * <b>discovery</b>:
     * Uses the Spring Discovery Client and a standard Spring RestTemplate class
     * @see com.optimagrowth.license.service.client.OrganizationDiscoveryClient
     *
     * <b>rest</b>:
     * Uses an enhanced Spring RestTemplate to invoke the Load Balancer service
     * @see com.optimagrowth.license.service.client.OrganizationRestTemplateClient
     *
     * <b>feign</b>:
     * Uses Netflix’s Feign client library to invoke a service via the Load Balancer
     * @see com.optimagrowth.license.service.client.OrganizationFeignClient
     */
    @GetMapping(value = "/{licenseId}/{clientType}")
    public ResponseEntity<License> getLicenseWithEurekaClient(
            @PathVariable("organizationId") String organizationId,
            @PathVariable("licenseId") String licenseId,
            @PathVariable("clientType") String clientType,
            @RequestHeader(value = "Accept-Language", required = false) Locale locale) {
        License license = licenseService.getLicense(licenseId, organizationId, clientType, locale);
        return ResponseEntity.ok(license);
    }

    @PutMapping
    public ResponseEntity<String> updateLicense(
            @RequestBody License requestBody,
            @RequestHeader(value = "Accept-Language", required = false) Locale locale) {
        License updated = licenseService.updateLicense(requestBody);
        String message = messages.getMessage("license.update.message", null, locale);
        String responseMessage = String.format(message, updated);
        return ResponseEntity.ok(responseMessage);
    }

    @PostMapping
    public ResponseEntity<String> createLicense(
            @PathVariable("organizationId") String organizationId,
            @RequestBody License requestBody,
            @RequestHeader(value = "Accept-Language", required = false) Locale locale) {
        License created = licenseService.createLicense(requestBody, organizationId);
        String message = messages.getMessage("license.create.message", null, locale);
        String responseMessage = String.format(message, created);
        return ResponseEntity.ok(responseMessage);
    }

    @DeleteMapping(value = "/{licenseId}")
    public ResponseEntity<String> deleteLicense(
            @PathVariable("organizationId") String organizationId,
            @PathVariable("licenseId") String licenseId,
            @RequestHeader(value = "Accept-Language", required = false) Locale locale) {
        licenseService.deleteLicense(licenseId, organizationId);
        String message = messages.getMessage("license.delete.message", null, locale);
        String responseMessage = String.format(message, licenseId, organizationId);
        return ResponseEntity.ok(responseMessage);
    }
}
