package com.roarstead.Components.Serializing;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.roarstead.Components.Annotation.DeExclude;

public class DeExcludeStrategy implements ExclusionStrategy  {
    @Override
    public boolean shouldSkipField(FieldAttributes field) {
        return field.getAnnotation(DeExclude.class) != null;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }
}
