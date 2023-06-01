package com.roarstead.Components.Auth.Rbac;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class RbacConfig {
    public abstract Map<Integer, List<String>> getUsersRoles();

    public abstract Map<String, List<String>> getRolesPerms();

    public abstract Map<String, Rule> getPermsRule();
}
