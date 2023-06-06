package com.roarstead.Components.Validation;

import com.roarstead.Components.Annotation.ValidPhoneNumber;
import com.roarstead.Components.Annotation.ValidUsername;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UsernameValidator implements ConstraintValidator<ValidUsername, String> {
    private static final String USERNAME_PATTERN = "^(?=.{8,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$";

    @Override
    public void initialize(ValidUsername constraintAnnotation) {
        // Initialization logic, if needed
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (username == null) {
            return false;  // null values are considered invalid
        }
        // Validate the phone number using the pattern
        return username.matches(USERNAME_PATTERN);
    }
}
