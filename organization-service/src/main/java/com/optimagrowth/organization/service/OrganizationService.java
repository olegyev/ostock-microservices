package com.optimagrowth.organization.service;

import com.optimagrowth.organization.config.ServiceConfig;
import com.optimagrowth.organization.model.Organization;
import com.optimagrowth.organization.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrganizationService {

    @Autowired
    private MessageSource messages;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private ServiceConfig config;

    public Organization getOrganization(String organizationId, Locale locale) {
        Optional<Organization> organization = organizationRepository.findById(organizationId);
        return organization.orElse(null);
    }

    public Organization create(Organization organization) {
        organization.setId(UUID.randomUUID().toString());
        organization = organizationRepository.save(organization);
        return organization;

    }

    public Organization update(Organization organization) {
        return organizationRepository.save(organization);
    }

    public void delete(String organizationId) {
        organizationRepository.deleteById(organizationId);
    }
}
