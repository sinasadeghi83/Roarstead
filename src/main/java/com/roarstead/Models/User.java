package com.roarstead.Models;

import com.roarstead.Components.Auth.Models.Auth;
import com.roarstead.Components.Exceptions.InvalidPasswordException;
import com.roarstead.Components.Exceptions.ModelNotFoundException;
import com.roarstead.Components.Exceptions.NotAuthenticatedException;
import jakarta.persistence.Column;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
public class User extends Auth {
    @Column(nullable = false, unique = true)
    private String username;  //unique
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(unique = true)
    private String email;     //unique and formatting
    @Column(unique = true)
    private String phone;

    @Embedded
    private Country country;

    @Temporal(TemporalType.DATE)
    @Column(name="birth_date", nullable = false)
    private Date birthDate;

    @ManyToMany
    @JoinTable(name = "roar_like", joinColumns = @JoinColumn(name = "username"), inverseJoinColumns = @JoinColumn(name = "roar-id"))
    private Set<Roar> likedRoars;

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
  
    public void like(Roar roar) {
        likedRoars.add(roar);
    }

    public void unLike(Roar roar) {
        likedRoars.remove(roar);
    }

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
}