package com.hieudt79.identity_service.utils;

import com.hieudt79.identity_service.dto.req.ResetPassRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchValidator implements ConstraintValidator<com.hieudt79.identity_service.dto.validator.PasswordMatchValidator, Object> {
    @Override
    public void initialize(com.hieudt79.identity_service.dto.validator.PasswordMatchValidator constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        ResetPassRequest request = (ResetPassRequest) o;
        return request.getPassword().equals(request.getConfirmPassword());
    }
}
