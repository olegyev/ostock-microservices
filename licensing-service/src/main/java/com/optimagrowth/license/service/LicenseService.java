package com.optimagrowth.license.service;

import com.optimagrowth.license.config.ServiceConfig;
import com.optimagrowth.license.model.License;
import com.optimagrowth.license.model.Organization;
import com.optimagrowth.license.repository.LicenseRepository;
import com.optimagrowth.license.service.client.OrganizationDiscoveryClient;
import com.optimagrowth.license.service.client.OrganizationFeignClient;
import com.optimagrowth.license.service.client.OrganizationRestTemplateClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeoutException;

@Service
public class LicenseService {

    @Autowired
    private MessageSource messages;

    @Autowired
    private LicenseRepository licenseRepository;

    @Autowired
    private ServiceConfig config;

    @Autowired
    private OrganizationFeignClient organizationFeignClient;

    @Autowired
    private OrganizationRestTemplateClient organizationRestTemplateClient;

    @Autowired
    private OrganizationDiscoveryClient organizationDiscoveryClient;

    public License getLicense(String licenseId, String organizationId, Locale locale) {
        License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
        if (null == license) {
            throw new IllegalArgumentException(String.format(
                    messages.getMessage("license.search.error.message", null, locale),
                    licenseId,
                    organizationId));
        }
        return license.withComment(config.getProperty());
    }

    public License getLicense(String licenseId, String organizationId, String clientType, Locale locale) {
        License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);

        if (null == license) {
            throw new IllegalArgumentException(String.format(
                    messages.getMessage("license.search.error.message", null, locale),
                    licenseId,
                    organizationId));
        }

        Organization organization = retrieveOrganizationInfo(organizationId, clientType);

        if (null != organization) {
            license.setOrganizationName(organization.getName());
            license.setContactName(organization.getContactName());
            license.setContactEmail(organization.getContactEmail());
            license.setContactPhone(organization.getContactPhone());
        }

        return license.withComment(config.getProperty());
    }

    private Organization retrieveOrganizationInfo(String organizationId, String clientType) {
        return switch (clientType) {
            case "discovery" -> {
                System.out.println("I am using the discovery client");
                yield organizationDiscoveryClient.getOrganization(organizationId);
            }
            case "rest" -> {
                System.out.println("I am using the rest client");
                yield organizationRestTemplateClient.getOrganization(organizationId);
            }
            case "feign" -> {
                System.out.println("I am using the feign client");
                yield organizationFeignClient.getOrganization(organizationId);
            }
            default -> organizationRestTemplateClient.getOrganization(organizationId);
        };
    }

    /**
     * For testing
     * @link <a href="http://localhost:8080/actuator/circuitbreakers">circuit breakers' events endpoint</a>
     */
    @CircuitBreaker(name = "licenseService", fallbackMethod = "buildFallbackLicenseList")
    public List<License> getLicensesByOrganization(String organizationId) throws TimeoutException {
        randomlyRunLong();
        return licenseRepository.findByOrganizationId(organizationId);
    }

    // Gives us a one-in-three chance of a database call running long
    private void randomlyRunLong() throws TimeoutException {
        Random rand = new Random();
        int randomNum = rand.nextInt(3) + 1;
        if (randomNum == 3) sleep();
    }

    private void sleep() throws TimeoutException {
        try {
            Thread.sleep(5000);
            throw new java.util.concurrent.TimeoutException();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    private List<License> buildFallbackLicenseList(String organizationId, Throwable t) {
        List<License> fallbackList = new ArrayList<>();

        License fallbackLicense = new License();
        fallbackLicense.setLicenseId("0000000-00-00000");
        fallbackLicense.setOrganizationId(organizationId);
        fallbackLicense.setComment("Sorry, no licensing information currently available");

        fallbackList.add(fallbackLicense);
        return fallbackList;
    }

    public License createLicense(License license, String organizationId) {
        License created = null;

        if (license != null) {
            license.setLicenseId(UUID.randomUUID().toString());
            license.setOrganizationId(organizationId);
            licenseRepository.save(license);
            created = license.withComment(config.getProperty());
        }

        return created;
    }

    public License updateLicense(License license) {
        License updated = null;

        if (license != null) {
            licenseRepository.save(license);
            updated = license.withComment(config.getProperty());
        }

        return updated;
    }

    public void deleteLicense(String licenseId, String organizationId) {
        License license = new License();
        license.setLicenseId(licenseId);
        license.setOrganizationId(licenseId);
        licenseRepository.delete(license);
    }
}
