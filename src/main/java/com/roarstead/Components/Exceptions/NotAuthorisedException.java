package com.roarstead.Components.Exceptions;

public class NotAuthorisedException extends RuntimeException {
    @Override
    public String getMessage() {
        return "permission-denied";
    }
}
