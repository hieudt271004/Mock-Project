package com.hieudt79.identity_service.client.feign.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hieudt79.identity_service.utils.enums.Gender;
import com.hieudt79.identity_service.utils.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterDTO {
    private String fullName;
    private Gender gender;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate bod;
    private String phone;
    private String email;
    private UserStatus status;
}
