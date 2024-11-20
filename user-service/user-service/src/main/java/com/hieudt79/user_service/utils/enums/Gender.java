package com.hieudt79.user_service.utils.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Gender{

    @JsonProperty("MALE")
    MALE,

    @JsonProperty("FEMALE")
    FEMALE,

    @JsonProperty("OTHER")
    OTHER;
}
