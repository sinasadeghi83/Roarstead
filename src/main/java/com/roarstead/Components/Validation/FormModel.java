package com.roarstead.Components.Validation;

import com.roarstead.Models.UserForm;
import jakarta.validation.ConstraintViolation;

import java.util.Set;

public interface FormModel<T> {
    public boolean validate();
    public Set<ConstraintViolation<T>> getViolations();
}
