package com.hieudt79.user_service.utils.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum UserStatus {

    @JsonProperty("ACTIVE")
    ACTIVE,

    @JsonProperty("INACTIVE")
    INACTIVE,

    @JsonProperty("NONE")
    NONE;
}
