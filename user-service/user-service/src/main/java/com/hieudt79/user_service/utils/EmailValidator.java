package com.hieudt79.user_service.utils;

import com.hieudt79.user_service.dto.validator.EmailValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<EmailValid, String> {

    private Pattern pattern;
    private Matcher matcher;
    private static final String EMAIL_PATTERN =  """
        ^[_A-Za-z0-9-+]+
        (.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(.[A-Za-z0-9]+)*
        (.[A-Za-z]{2,})$""";

    @Override
    public void initialize(EmailValid constraintAnnotation) {
        //ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return isEmailValid(email);
    }

    private boolean isEmailValid(String email) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
