package com.optimagrowth.license.service;

import com.optimagrowth.license.model.License;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Random;

@Service
public class LicenseService {

    @Autowired
    MessageSource messages;

    public License getLicense(String licenseId, String organizationId) {
        License license = new License();

        license.setId(new Random().nextInt(1000));
        license.setLicenseId(licenseId);
        license.setOrganizationId(organizationId);
        license.setDescription("Software Product");
        license.setProductName("Ostock");
        license.setLicenseType("full");

        return license;
    }

    public String createLicense(License license, String organizationId, Locale locale) {
        String responseMessage = null;

        if (license != null) {
            license.setOrganizationId(organizationId);
            String message = messages.getMessage("license.create.message", null, locale);
            responseMessage = String.format(message, license);
        }

        return responseMessage;
    }

    public String updateLicense(License license, String organizationId, Locale locale) {
        String responseMessage = null;

        if (license != null) {
            license.setOrganizationId(organizationId);
            String message = messages.getMessage("license.update.message", null, locale);
            responseMessage = String.format(message, license);
        }

        return responseMessage;
    }

    public String deleteLicense(String licenseId, String organizationId, Locale locale) {
        String responseMessage = null;
        String message = messages.getMessage("license.delete.message", null, locale);
        responseMessage = String.format(message, licenseId, organizationId);
        return responseMessage;
    }

}
