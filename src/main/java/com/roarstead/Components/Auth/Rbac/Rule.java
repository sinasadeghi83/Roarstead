package com.roarstead.Components.Auth.Rbac;

import com.roarstead.Components.Auth.Models.Auth;

import java.util.Map;

public abstract class Rule {
    /**
     * Thanks to Yii framework developers for PHP for giving me these notions and ideas
     * https://www.yiiframework.com/doc/guide/2.0/en/security-authorization
     * @param auth Auth the user object.
     * @param item String the role or permission that this rule is associated with
     * @param params HashMap parameters passed to.
     * @return bool a value indicating whether the rule permits the role or permission it is associated with.
     */
    public abstract boolean execute(Auth auth, String item, Map<String, Object> params);
}
