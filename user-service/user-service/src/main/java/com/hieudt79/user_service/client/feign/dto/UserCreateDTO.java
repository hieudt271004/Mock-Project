package com.hieudt79.user_service.client.feign.dto;

import com.hieudt79.user_service.utils.enums.UserRole;
import com.hieudt79.user_service.utils.enums.UserStatus;
import lombok.*;
import org.springframework.stereotype.Service;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateDTO {
    private String email;
    private String password;
    private UserStatus status;
    private UserRole role;
}
