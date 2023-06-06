package com.roarstead.Components.Validation;

import com.google.gson.reflect.TypeToken;
import com.roarstead.App;
import com.roarstead.Components.Annotation.ValidCountry;
import com.roarstead.Components.Business.Models.Country;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CountryValidator implements ConstraintValidator<ValidCountry, String> {
    @Override
    public void initialize(ValidCountry constraintAnnotation) {
        // Initialization logic, if needed
    }

    @Override
    public boolean isValid(String dialCode, ConstraintValidatorContext context) {
        if (dialCode == null) {
            return false;  // null values are considered invalid
        }

        // Validation logic for the Country object
       return Country.getCountryByDialCode(dialCode) != null;
    }
}
