package com.roarstead.Components.Auth.Models;

import com.roarstead.Components.Annotation.Exclude;
import com.roarstead.Components.Exceptions.NotAuthenticatedException;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Auth {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    protected int id;

    @Column(nullable = false, unique = true)
    protected String username;  //unique

    @Exclude
    @Column(nullable = false)
    protected String password;

    @ManyToMany
    @JoinTable(
            name = "auth_roles",
            joinColumns = @JoinColumn(name="auth_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Exclude
    protected Set<Role> roles;

    public Auth(){
    }

    public Auth(String password){
        this.password = password;
    }

    public abstract void enterPassword(String password);
    public abstract Auth identity() throws NotAuthenticatedException;

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role role){
        if(this.roles == null)
            roles = new HashSet<>();
        this.roles.add(role);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}