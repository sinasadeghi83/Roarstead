package com.roarstead.Components.Auth;

import com.roarstead.App;
import com.roarstead.Components.Auth.Models.Auth;
import com.roarstead.Components.Auth.Models.Permission;
import com.roarstead.Components.Auth.Models.Role;
import com.roarstead.Components.Auth.Rbac.Rule;
import com.roarstead.Components.Database.Database;
import com.roarstead.Components.Exceptions.InvalidPasswordException;
import com.roarstead.Components.Exceptions.ModelNotFoundException;
import com.roarstead.Components.Exceptions.NotAuthenticatedException;
import jakarta.persistence.NoResultException;
import org.hibernate.query.Query;

import java.util.*;

public class AuthManager {
    private Auth auth = null;

    //Permission -> Rule
    private Map<String, Rule> permsRule;

    public AuthManager(){
        this.permsRule = App.getCurrentApp().getRbacConfig().getPermsRule();
    }

    public int getUserId(){
        return auth.getId();
    }

    public Auth identity() throws NotAuthenticatedException {
        return auth == null ? null : auth.identity();
    }

    public void assignRole(Auth auth, Role role){
        Database db = App.getCurrentApp().getDb();
        db.ready();
        auth.addRole(role);
        db.getSession().save(auth);
        db.done();
    }

    public boolean authorise(List<String> rolesPerms) throws NotAuthenticatedException {
        Database db = App.getCurrentApp().getDb();

        String query = "SELECT count(r) FROM Role r JOIN r.auths a WHERE a.id=:userId AND r.name IN (:rolesPerms)";
        Query<Long> rolesQuery = db.getSession().createQuery(query);
        rolesQuery.setParameter("userId", this.getUserId());
        rolesQuery.setParameterList("rolesPerms", rolesPerms);
        long rolesCount = rolesQuery.getSingleResult();
        if(rolesCount > 0){
            return true;
        }

        query = "SELECT p FROM Permission p JOIN p.roles r JOIN r.auths a WHERE a.id=:userId AND p.name IN (:rolesPerms)";
        Query<Permission> permsQuery = db.getSession().createQuery(query);
        permsQuery.setParameter("userId", this.getUserId());
        permsQuery.setParameterList("rolesPerms", rolesPerms);

        List<Permission> perms = permsQuery.getResultList();

        for (Permission perm :
                perms) {
            if(this.can(perm.getName())){
                return true;
            }
        }

        return false;
    }

    public boolean can(String perm){
        return this.can(perm, new HashMap<>());
    }

    public boolean can(String perm, Map<String, Object> params){
        String query = "SELECT p FROM Permission p JOIN p.roles r JOIN r.auths a WHERE a.id=:userId AND p.name=:perm";
        Query<Permission> permsQuery = App.getCurrentApp().getDb().getSession().createQuery(query);
        permsQuery.setParameter("userId", this.getUserId());
        permsQuery.setParameter("perm", perm);

        Permission permObj;
        try {
            permObj = permsQuery.getSingleResult();
        }catch (NoResultException e){
            return false;
        }

        if(permsRule.containsKey(permObj.getName())){
            Rule rule = permsRule.get(permObj.getName());
            return rule.execute(this.auth, perm, params);
        }
        return true;
    }

    public void authenticate(Auth auth) throws ModelNotFoundException, InvalidPasswordException {
        this.auth = auth.authenticate();
    }
}