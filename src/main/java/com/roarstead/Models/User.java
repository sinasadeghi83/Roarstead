package com.roarstead.Models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "users")
public class User {

    public User(){
    }

    public User(String username, String firstName, String lastName, String phone, String password, String country, Date birthDate) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.password = password;
        this.country = country;
        this.birthDate = birthDate;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(name="first_name", nullable = false)
    private String firstName;

    @Column(name="last_name", nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String country;

    @Temporal(TemporalType.DATE)
    private Date birthDate;


}
