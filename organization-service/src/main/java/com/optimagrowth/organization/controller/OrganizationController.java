package com.optimagrowth.organization.controller;

import com.optimagrowth.organization.events.model.ActionEnum;
import com.optimagrowth.organization.events.producer.OrganizationChangeSource;
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
public class OrganizationController {

    @Autowired
    private MessageSource messages;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private OrganizationChangeSource organizationChangeSource;

    @GetMapping(value = "/{organizationId}")
    public ResponseEntity<Organization> getOrganization(
            @PathVariable("organizationId") String organizationId) {
        Organization organization = organizationService.getOrganization(organizationId);

        if (organization == null) return ResponseEntity.ok(null);

        organization.add(
                linkTo(methodOn(OrganizationController.class).getOrganization(organizationId)).withSelfRel(),
                linkTo(methodOn(OrganizationController.class).createOrganization(organization, null)).withRel("createLicense"),
                linkTo(methodOn(OrganizationController.class).updateOrganization(organization, null)).withRel("updateLicense"),
                linkTo(methodOn(OrganizationController.class).deleteLicense(organizationId, null)).withRel("deleteLicense")
        );

        // Uncomment if you want to test a Kafka pub/sub in more near-real-time mode
        // organizationChangeSource.delegateOrganizationChangeEventSupplier(organizationId, ActionEnum.GET);

        return ResponseEntity.ok(organization);
    }

    @PutMapping
    public ResponseEntity<String> updateOrganization(
            @RequestBody Organization requestBody,
            @RequestHeader(value = "Accept-Language", required = false) Locale locale) {
        Organization updated = organizationService.update(requestBody);
        String message = messages.getMessage("organization.update.message", null, locale);
        String responseMessage = String.format(message, updated);

        organizationChangeSource.delegateOrganizationChangeEventSupplier(updated.getId(), ActionEnum.UPDATED);

        return ResponseEntity.ok(responseMessage);
    }

    @PostMapping
    public ResponseEntity<String> createOrganization(
            @RequestBody Organization requestBody,
            @RequestHeader(value = "Accept-Language", required = false) Locale locale) {
        Organization created = organizationService.create(requestBody);
        String message = messages.getMessage("organization.create.message", null, locale);
        String responseMessage = String.format(message, created);

        organizationChangeSource.delegateOrganizationChangeEventSupplier(created.getId(), ActionEnum.CREATED);

        return ResponseEntity.ok(responseMessage);
    }

    @DeleteMapping(value = "/{organizationId}")
    public ResponseEntity<String> deleteLicense(
            @PathVariable("organizationId") String organizationId,
            @RequestHeader(value = "Accept-Language", required = false) Locale locale) {
        organizationService.delete(organizationId);
        String message = messages.getMessage("organization.delete.message", null, locale);
        String responseMessage = String.format(message, organizationId);

        organizationChangeSource.delegateOrganizationChangeEventSupplier(organizationId, ActionEnum.DELETED);

        return ResponseEntity.ok(responseMessage);
    }
}
