package com.roarstead.Components.Exceptions;

public class InvalidPasswordException extends RuntimeException {
    @Override
    public String getMessage() {
        return "invalid-pass";
    }
}
