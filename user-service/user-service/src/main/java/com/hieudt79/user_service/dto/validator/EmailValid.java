package com.hieudt79.user_service.dto.validator;

import com.hieudt79.user_service.utils.EmailValidator;
import com.hieudt79.user_service.utils.PhoneValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EmailValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailValid {
    String message() default "Invalid mail";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
