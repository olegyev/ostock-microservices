package com.optimagrowth.license.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;


// This is fundamentally INCORRECT to set Entity as a REST HATEOAS RepresentationModel,
// according to the clean architecture requirements.
// DTO pattern should be used instead.
// In this case, this is done intentionally for simplicity.

@Getter
@Setter
@ToString
@Entity
@Table(name = "licenses")
public class License extends RepresentationModel<License> {

    @Id
    @Column(name = "license_id", nullable = false)
    private String licenseId;

    private String description;

    @Column(name = "organization_id", nullable = false)
    private String organizationId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "license_type", nullable = false)
    private String licenseType;

    private String comment;

    @Transient
    private String organizationName;

    @Transient
    private String contactName;

    @Transient
    private String contactEmail;

    @Transient
    private String contactPhone;

    public License withComment(String comment) {
        this.setComment(comment);
        return this;
    }
}
