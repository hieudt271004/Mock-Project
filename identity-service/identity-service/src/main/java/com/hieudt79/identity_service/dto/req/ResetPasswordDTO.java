package com.hieudt79.identity_service.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ResetPasswordDTO {

    @NotBlank(message = "secretKey must be not blank")
    private String secretKey;

    @NotBlank(message = "password must be not blank")
    private String password;

    @NotBlank(message = "confirmPassword must be not blank")
    private String confirmPassword;

}