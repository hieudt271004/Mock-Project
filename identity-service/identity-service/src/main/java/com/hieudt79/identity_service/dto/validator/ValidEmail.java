package com.hieudt79.identity_service.dto.validator;

import com.hieudt79.identity_service.utils.EmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.FIELD,ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
@Documented
public @interface ValidEmail {
    String message() default "Invalid mail";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
