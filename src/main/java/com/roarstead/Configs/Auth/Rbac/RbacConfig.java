package com.roarstead.Configs.Auth.Rbac;

import com.roarstead.Components.Auth.Rbac.Rule;

import java.util.HashMap;
import java.util.Map;

public class RbacConfig extends com.roarstead.Components.Auth.Rbac.RbacConfig {

    @Override
    public Map<String, Rule> getPermsRule(){
        return new HashMap<>(Map.of(
//                "removeUser", new RemoveUserRule(),
//                "changeResource", new ChangeResourceRule(),
//                "borrow", new BorrowRule()
        ));
    }
}
