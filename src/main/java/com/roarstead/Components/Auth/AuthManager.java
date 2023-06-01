package com.roarstead.Components.Auth;

import com.roarstead.App;
import com.roarstead.Components.Auth.Models.Auth;
import com.roarstead.Components.Auth.Rbac.Rule;
import com.roarstead.Components.Exceptions.InvalidPasswordException;
import com.roarstead.Components.Exceptions.ModelNotFoundException;
import com.roarstead.Components.Exceptions.NotAuthenticatedException;
import com.roarstead.Configs.Rbac.RbacConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthManager {
    private Auth auth = null;
    //User id -> roles
    private Map<Integer, List<String>> usersRoles;
    //Roles -> permissions
    private Map<String, List<String>> rolesPerms;
    //Permssion -> Rule
    private Map<String, Rule> permsRule;

    public AuthManager(){
        this.usersRoles = App.getCurrentApp().getRbacConfig().getUsersRoles();
        this.rolesPerms = App.getCurrentApp().getRbacConfig().getRolesPerms();
        this.permsRule = App.getCurrentApp().getRbacConfig().getPermsRule();
    }

    public int getUserId(){
        return auth.getId();
    }

    public Auth identity() throws NotAuthenticatedException {
        return auth == null ? null : auth.identity();
    }

    public void assignRole(Auth auth, String role){
        List<String> userRoles = usersRoles.computeIfAbsent(auth.getId(), k -> new ArrayList<>());
        userRoles.add(role);
        if(!rolesPerms.containsKey(role)){
            rolesPerms.put(role, new ArrayList<>());
        }
    }

    public boolean authorise(List<String> rolesPerms) throws NotAuthenticatedException {
        List<String> userRoles = usersRoles.get(this.getUserId());
        if(userRoles == null){
            return false;
        }
        for(String rolePerm:
                rolesPerms){
            if(userRoles.contains(rolePerm)){
                return true;
            }
            if(this.can(rolePerm)){
                return true;
            }
        }
        return false;
    }

    public boolean can(String perm){
        return this.can(perm, new HashMap<>());
    }

    public boolean can(String perm, Map<String, Object> params){
        List<String> userRoles = usersRoles.get(this.getUserId());
        for (String userRole :
                userRoles) {
            List<String> perms = rolesPerms.get(userRole);
            if (perms.contains(perm)) {
                if(permsRule.containsKey(perm)) {
                    Rule rule = permsRule.get(perm);
                    return rule.execute(this.auth, perm, params);
                }
                return true;
            }
        }
        return false;
    }

    public void authenticate(Auth auth) throws ModelNotFoundException, InvalidPasswordException {
        this.auth = auth.authenticate();
    }
}
