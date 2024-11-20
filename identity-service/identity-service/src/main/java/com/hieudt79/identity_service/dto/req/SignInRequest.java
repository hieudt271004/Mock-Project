package com.hieudt79.identity_service.dto.req;

import com.hieudt79.identity_service.dto.validator.ValidEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignInRequest {
    @ValidEmail(message = "invalid email")
    @NotBlank(message = "email can not be invalid")
    private String email;

    @NotBlank(message = "Password can not be blank")
    private String password;
}
