package com.roarstead.Models;

import com.google.gson.annotations.SerializedName;
import com.roarstead.Components.Auth.Models.Auth;
import com.roarstead.Components.Business.Models.Country;
import com.roarstead.Components.Exceptions.NotAuthenticatedException;
import jakarta.persistence.Column;

import jakarta.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends Auth {

    @Column(name = "first_name", nullable = false)
    @SerializedName("first_name")
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @SerializedName("last_name")
    private String lastName;

    @Column(unique = true)
    private String email;     //unique and formatting
    @Column(unique = true)
    private String phone;

    @Embedded
    private Country country;

    @Temporal(TemporalType.DATE)
    @Column(name="birth_date", nullable = false)
    @SerializedName("birth_date")
    private Date birthDate;

    @Embedded
    private Profile profile;

//    @ManyToMany
//    @JoinTable(name = "roar_like", joinColumns = @JoinColumn(name = "username"), inverseJoinColumns = @JoinColumn(name = "roar-id"))
//    private Set<Roar> likedRoars;

    public User() {

    }

    public User(String username, String firstName, String lastName, String email, String phone,Country country, String password, Date birthDate) {
        super(password);
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.country = country;
        this.birthDate = birthDate;
    }

    @Override
    public void enterPassword(String password) {
        setPassword(password);
    }
  
//    public void like(Roar roar) {
//        likedRoars.add(roar);
//    }
//
//    public void unLike(Roar roar) {
//        likedRoars.remove(roar);
//    }

    @Override
    public Auth identity() throws NotAuthenticatedException {
        return this;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}