package com.roarstead.Components.Exceptions;

public class ModelNotFoundException extends Exception {
    @Override
    public String getMessage() {
        return "not-found";
    }
}
