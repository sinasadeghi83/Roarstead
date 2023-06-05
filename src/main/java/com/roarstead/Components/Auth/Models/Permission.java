package com.roarstead.Components.Auth.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

import java.util.Set;

@Entity
public class Permission {
    @Id
    @GeneratedValue
    private int id;

    @ManyToMany(mappedBy = "permissions")
    Set<Role> roles;

    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
