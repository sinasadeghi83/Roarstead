package com.roarstead.Components.Exceptions;

public class NotAuthenticatedException extends Exception {
    @Override
    public String getMessage() {
        return "permission-denied";
    }
}
