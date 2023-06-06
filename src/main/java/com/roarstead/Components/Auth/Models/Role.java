package com.roarstead.Components.Auth.Models;

import com.roarstead.App;
import jakarta.persistence.*;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
public class Role {

    public static final String DEFAULT_NAME = "@";

    public Role(){}

    public Role(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name="parent_id", columnDefinition = "integer default 1")
    private Role parent;

    @OneToMany(mappedBy = "parent")
    private List<Role> subRoles = new ArrayList<>();

    @ManyToMany(mappedBy = "roles")
    private Set<Auth> auths;

    @ManyToMany
    private Set<Permission> permissions;

    @Column(unique = true)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getParent() {
        return parent;
    }

    public void setParent(Role parent) {
        this.parent = parent;
    }

    public List<Role> getSubRoles() {
        return subRoles;
    }

    public void setSubRoles(List<Role> subRoles) {
        this.subRoles = subRoles;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static Role findRoleByName(String name){
        String strQuery = "FROM Role r WHERE r.name=:name";
        Query<Role> roleQuery = App.getCurrentApp().getDb().getSession().createQuery(strQuery);
        roleQuery.setParameter("name", name);
        try {
            return roleQuery.getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }
}
