package com.hieudt79.identity_service.utils.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum UserStatus {

    @JsonProperty("ACTIVE")
    ACTIVE,

    @JsonProperty("INACTIVE")
    INACTIVE,

    @JsonProperty("NONE")
    NONE;
}
