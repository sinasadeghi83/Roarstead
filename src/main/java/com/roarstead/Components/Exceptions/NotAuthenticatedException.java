package com.roarstead.Components.Exceptions;

public class NotAuthenticatedException extends RuntimeException {
    @Override
    public String getMessage() {
        return "permission-denied";
    }
}
