package com.hieudt79.identity_service.client.feign.dto;

import com.hieudt79.identity_service.utils.enums.UserRole;
import com.hieudt79.identity_service.utils.enums.UserStatus;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDTO {
    private String email;
    private String password;
    private UserStatus status;
    private UserRole userRole;
}
