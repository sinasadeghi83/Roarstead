package com.roarstead.Models;

import com.google.gson.annotations.SerializedName;
import com.roarstead.Components.Annotation.ValidCountry;
import com.roarstead.Components.Annotation.ValidPassword;
import com.roarstead.Components.Annotation.ValidPhoneNumber;
import com.roarstead.Components.Annotation.ValidUsername;
import com.roarstead.Components.Business.Models.Country;
import com.roarstead.Components.Validation.FormModel;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.*;

import java.util.Date;
import java.util.Set;

public class UserForm implements FormModel<UserForm> {
    @NotNull
    @ValidUsername
    private String username;

    @NotBlank
    @Size(min=2, max=50, message = "First name must be between 3 to 50 characters")
    @SerializedName("first_name")
    private String firstName;

    @NotBlank
    @Size(min=2, max=50, message = "Last name must be between 3 to 50 characters")
    @SerializedName("last_name")
    private String lastName;

    @Email
    private String email;

    @ValidPhoneNumber
    private String phone;

    @NotBlank
    @ValidPassword
    private String password;

    @NotBlank
    @SerializedName("repeat_password")
    private String repeatPassword;

    @NotNull
    @ValidCountry
    @SerializedName("dial_code")
    private String dialCode;

    @NotNull
    @Past
    @SerializedName("birth_date")
    private Date birthDate;

    private Set<ConstraintViolation<UserForm>> violations = null;

    public UserForm(){}

    public UserForm(String username, String firstName, String lastName, String email, String phone, String password, String repeatPassword, String dialCode, Date birthDate) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.repeatPassword = repeatPassword;
        this.dialCode = dialCode;
        this.birthDate = birthDate;
    }

    @Override
    public boolean validate() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        this.violations = validator.validate(this);
        return this.violations.isEmpty();
    }

    @Override
    public Set<ConstraintViolation<UserForm>> getViolations() {
        return this.violations;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public String getDialCode() {
        return dialCode;
    }

    public void setDialCode(String dialCode) {
        this.dialCode = dialCode;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
}
