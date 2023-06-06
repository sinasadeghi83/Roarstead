package com.roarstead.Components.Annotation;

import com.roarstead.Components.Validation.PhoneNumberValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneNumberValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPhoneNumber {
    String message() default "Invalid phone number: ${validatedValue}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
