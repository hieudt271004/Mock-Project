package com.hieudt79.identity_service.dto.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = com.hieudt79.identity_service.utils.PasswordMatchValidator.class)
@Documented
public @interface PasswordMatchValidator {
    String message() default "Password not match";
    Class<?>[] group() default {};
    Class<? extends Payload>[] payload() default {};
}
