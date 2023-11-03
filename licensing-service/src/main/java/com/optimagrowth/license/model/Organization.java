package com.optimagrowth.license.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

// This is fundamentally INCORRECT to set Entity as a REST HATEOAS RepresentationModel,
// according to the clean architecture requirements.
// This POJO is used to be both cached in Redis and returned as a REST response.
// Only the database response should be cached rather than the entire REST HATEOAS.
// "extends RepresentationModel<Organization>" gives an Exception.
// See: https://stackoverflow.com/a/76313579

@Getter
@Setter
@ToString
@RedisHash("organization")
public class Organization {

    @Id
    private String id;
    private String name;
    private String contactName;
    private String contactEmail;
    private String contactPhone;
}
