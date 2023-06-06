package com.roarstead.Components.Validation;

import com.roarstead.Components.Annotation.ValidPhoneNumber;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {
    private static final String PHONE_NUMBER_PATTERN = "^[0-9]{10}$";

    @Override
    public void initialize(ValidPhoneNumber constraintAnnotation) {
        // Initialization logic, if needed
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if (phoneNumber == null) {
            return false;  // null values are considered invalid
        }

        //Remove first zero digit if there's any
        if(phoneNumber.charAt(0) == '0')
            phoneNumber = phoneNumber.substring(1);

        // Remove any non-digit characters from the phone number
        String digitsOnly = phoneNumber.replaceAll("\\D", "");

        // Validate the phone number using the pattern
        return digitsOnly.matches(PHONE_NUMBER_PATTERN);
    }
}
