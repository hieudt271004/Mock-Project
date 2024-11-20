package com.hieudt79.identity_service.utils.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum UserRole {

    @JsonProperty("ADMIN")
    ADMIN,

    @JsonProperty("USER")
    USER,
}
