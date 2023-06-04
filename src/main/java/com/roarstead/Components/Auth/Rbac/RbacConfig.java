package com.roarstead.Components.Auth.Rbac;

import java.util.Map;

public abstract class RbacConfig {
    public abstract Map<String, Rule> getPermsRule();
}
