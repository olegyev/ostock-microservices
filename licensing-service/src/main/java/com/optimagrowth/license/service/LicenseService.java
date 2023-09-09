package com.optimagrowth.license.service;

import com.optimagrowth.license.config.ServiceConfig;
import com.optimagrowth.license.model.License;
import com.optimagrowth.license.repository.LicenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.UUID;

@Service
public class LicenseService {

    @Autowired
    MessageSource messages;

    @Autowired
    private LicenseRepository licenseRepository;

    @Autowired
    ServiceConfig config;

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
        String responseMessage;
        License license = new License();
        license.setLicenseId(licenseId);
        license.setOrganizationId(licenseId);
        licenseRepository.delete(license);
    }
}
