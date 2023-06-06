package com.roarstead.Components.Annotation;

import com.roarstead.Components.Validation.CountryValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CountryValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCountry {
    String message() default "Invalid country";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
