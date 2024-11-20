package com.hieudt79.identity_service.client.feign.dto;

import com.hieudt79.identity_service.utils.enums.UserStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserStatusSetDTO {
    private String userName;
    private UserStatus status;
}