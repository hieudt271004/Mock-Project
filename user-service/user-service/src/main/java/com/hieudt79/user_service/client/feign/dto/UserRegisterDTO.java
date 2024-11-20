package com.hieudt79.user_service.client.feign.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hieudt79.user_service.utils.enums.Gender;
import com.hieudt79.user_service.utils.enums.UserStatus;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class UserRegisterDTO {
    private String fullName;
    private Gender gender;
    @JsonFormat(pattern = "MM/dd/yyyy")
    private LocalDate bod;
    private String password;
    private String email;
    private String phone;
    private UserStatus status;
}
