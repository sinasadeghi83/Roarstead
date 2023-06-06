package com.roarstead.Components.Validation;

import com.roarstead.Components.Annotation.ValidPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {
    private static final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        // Initialization logic, if needed
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return false;  // null values are considered invalid
        }
        // Validate the phone number using the pattern
        return password.matches(PASSWORD_PATTERN);
    }
}
