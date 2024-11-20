package com.hieudt79.user_service.utils.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum UserRole {

    @JsonProperty("ADMIN")
    ADMIN,

    @JsonProperty("MANAGER")
    MANAGER,

    @JsonProperty("USER")
    USER,
}
