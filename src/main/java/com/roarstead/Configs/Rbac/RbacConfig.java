package com.roarstead.Configs.Rbac;

import com.roarstead.Components.Auth.Rbac.Rule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RbacConfig extends com.roarstead.Components.Auth.Rbac.RbacConfig {
    public Map<Integer, List<String>> getUsersRoles(){
        return new HashMap<>(Map.of(
//                0, List.of("admin")
        ));
    }

    public Map<String, List<String>> getRolesPerms(){
        return new HashMap<>(Map.of(
//                "admin", List.of("addLibrary", "removeUser"),
//                "manager", List.of("changeResource"),
//                "student", List.of("borrow"),
//                "staff", List.of("borrow")
        ));
    }

    public Map<String, Rule> getPermsRule(){
        return new HashMap<>(Map.of(
//                "removeUser", new RemoveUserRule(),
//                "changeResource", new ChangeResourceRule(),
//                "borrow", new BorrowRule()
        ));
    }
}
