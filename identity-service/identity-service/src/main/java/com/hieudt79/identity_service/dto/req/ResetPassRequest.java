package com.hieudt79.identity_service.dto.req;

import com.hieudt79.identity_service.dto.validator.PasswordMatchValidator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Data
@Builder
@Valid
public class ResetPassRequest {

    @NotBlank(message = "Password can not be blank")
    private String password;

    @PasswordMatchValidator
    @NotBlank(message = "Can not be blank")
    private String confirmPassword;
}
