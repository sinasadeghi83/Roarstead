package com.roarstead.Components.Exceptions;

public class AuthNotFoundException extends Exception {
    @Override
    public String getMessage() {
        return "Auth not found";
    }
}
