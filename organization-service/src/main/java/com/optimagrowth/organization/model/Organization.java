package com.optimagrowth.organization.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "organizations")
public class Organization extends RepresentationModel<Organization> {

    @Id
    @Column(name = "organization_id", nullable = false)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(name = "contact_name", nullable = false)
    private String contactName;

    @Column(name = "contact_email", nullable = false)
    private String contactEmail;

    @Column(name = "contact_phone", nullable = false)
    private String contactPhone;
}
