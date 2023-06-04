package com.roarstead.Components.Exceptions;

public class ModelNotFoundException extends RuntimeException {
    @Override
    public String getMessage() {
        return "not-found";
    }
}
