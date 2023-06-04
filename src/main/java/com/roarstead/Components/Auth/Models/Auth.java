package com.roarstead.Components.Auth.Models;

import com.roarstead.App;
import com.roarstead.Components.Exceptions.InvalidPasswordException;
import com.roarstead.Components.Exceptions.ModelNotFoundException;
import com.roarstead.Components.Exceptions.NotAuthenticatedException;
import jakarta.persistence.*;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Map;
import java.util.Set;

@MappedSuperclass
public abstract class Auth {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected int id;

    @Column(nullable = false)
    protected String password;

    @ManyToMany
    @JoinTable(
            name = "auth_roles",
            joinColumns = @JoinColumn(name="auth_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    protected Set<Role> roles;

    public Auth(){
    }

    public Auth(String password){
        this.password = password;
    }

    public abstract void enterPassword(String password);
    public abstract Auth identity() throws NotAuthenticatedException;

    public Auth authenticate() throws InvalidPasswordException, ModelNotFoundException{
        String strQuery = "SELECT a FROM Auth a WHERE a.id=:authId";
        Query<Auth> query = App.getCurrentApp().getDb().getSession().createQuery(strQuery);
        Auth auth;
        try {
            auth = query.getSingleResult();
        }catch (NoResultException e){
            throw new ModelNotFoundException();
        }

        if(!auth.getPassword().equals(this.getPassword())){
            throw new InvalidPasswordException();
        }
        return auth;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role role){
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
}