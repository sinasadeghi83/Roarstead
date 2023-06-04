package com.roarstead.Components.Auth.Models;

import com.roarstead.Components.Exceptions.InvalidPasswordException;
import com.roarstead.Components.Exceptions.ModelNotFoundException;
import com.roarstead.Components.Exceptions.NotAuthenticatedException;
import jakarta.persistence.*;

import java.util.Set;

@MappedSuperclass
public abstract class Auth {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected int id;

    @ManyToMany
    @JoinTable(
            name = "auth_roles",
            joinColumns = @JoinColumn(name="auth_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    protected Set<Role> roles;

    public abstract int getId();
    public abstract void setId(int id);
    public abstract void enterPassword(String password);
    public abstract String getPassword();
    public abstract Auth identity() throws NotAuthenticatedException;
    public abstract Auth authenticate() throws InvalidPasswordException, ModelNotFoundException;

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role role){
        this.roles.add(role);
    }
}