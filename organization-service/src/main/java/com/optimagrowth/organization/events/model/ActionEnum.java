package com.optimagrowth.organization.events.model;

import lombok.Getter;

@Getter
public enum ActionEnum {

    GET("GET"),
    CREATED("CREATED"),
    UPDATED("UPDATED"),
    DELETED("DELETED");

    private final String value;

    ActionEnum(String value) {
        this.value = value;
    }
}
