package com.optimagrowth.organization.controller;

import com.optimagrowth.organization.model.Organization;
import com.optimagrowth.organization.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "v1/organization")
public class LicenseController {

    @Autowired
    MessageSource messages;

    @Autowired
    OrganizationService organizationService;

    @GetMapping(value = "/{organizationId}")
    public ResponseEntity<Organization> getOrganization(
            @PathVariable("organizationId") String organizationId,
            @PathVariable("licenseId") String licenseId,
            @RequestHeader(value = "Accept-Language", required = false) Locale locale) {
        Organization organization = organizationService.getOrganization(organizationId, locale);

        organization.add(
                linkTo(methodOn(LicenseController.class).getOrganization(organizationId, null, locale)).withSelfRel(),
                linkTo(methodOn(LicenseController.class).createOrganization(organization, null)).withRel("createLicense"),
                linkTo(methodOn(LicenseController.class).updateOrganization(organization, null)).withRel("updateLicense"),
                linkTo(methodOn(LicenseController.class).deleteLicense(organizationId, null)).withRel("deleteLicense")
        );

        return ResponseEntity.ok(organization);
    }

    @PutMapping
    public ResponseEntity<String> updateOrganization(
            @RequestBody Organization requestBody,
            @RequestHeader(value = "Accept-Language", required = false) Locale locale) {
        Organization updated = organizationService.update(requestBody);
        String message = messages.getMessage("organization.update.message", null, locale);
        String responseMessage = String.format(message, updated);
        return ResponseEntity.ok(responseMessage);
    }

    @PostMapping
    public ResponseEntity<String> createOrganization(
            @RequestBody Organization requestBody,
            @RequestHeader(value = "Accept-Language", required = false) Locale locale) {
        Organization created = organizationService.create(requestBody);
        String message = messages.getMessage("organization.create.message", null, locale);
        String responseMessage = String.format(message, created);
        return ResponseEntity.ok(responseMessage);
    }

    @DeleteMapping(value = "/{licenseId}")
    public ResponseEntity<String> deleteLicense(
            @PathVariable("organizationId") String organizationId,
            @RequestHeader(value = "Accept-Language", required = false) Locale locale) {
        organizationService.delete(organizationId);
        String message = messages.getMessage("organization.delete.message", null, locale);
        String responseMessage = String.format(message, organizationId);
        return ResponseEntity.ok(responseMessage);
    }
}
