package com.roarstead.Models;

import com.google.gson.annotations.SerializedName;
import com.roarstead.Components.Validation.FormModel;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.util.Set;

public class ProfileForm implements FormModel<ProfileForm> {

    private Set<ConstraintViolation<ProfileForm>> violations = null;

    @Size(max = 160, message = "Bio must be less than 161 characters")
    private String bio;

    @Size(max = 22, message = "Location must be less than 23")
    private String location;

    @SerializedName("url")
    @URL
    private String websiteLink;

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWebsiteLink() {
        return websiteLink;
    }

    public void setWebsiteLink(String websiteLink) {
        this.websiteLink = websiteLink;
    }

    @Override
    public boolean validate() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        this.violations = validator.validate(this);
        return this.violations.isEmpty();
    }

    @Override
    public Set<ConstraintViolation<ProfileForm>> getViolations() {
        return this.violations;
    }
}
