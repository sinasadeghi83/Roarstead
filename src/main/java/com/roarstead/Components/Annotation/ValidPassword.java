package com.roarstead.Components.Annotation;

import com.roarstead.Components.Validation.PasswordValidator;
import com.roarstead.Components.Validation.UsernameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
    String message() default "Invalid password your password must be at least 8 characters and consist of both number and letter";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
