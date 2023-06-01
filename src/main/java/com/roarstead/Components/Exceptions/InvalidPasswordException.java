package com.roarstead.Components.Exceptions;

public class InvalidPasswordException extends Exception {
    @Override
    public String getMessage() {
        return "invalid-pass";
    }
}
