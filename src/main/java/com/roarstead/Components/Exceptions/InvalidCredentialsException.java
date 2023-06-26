package com.roarstead.Components.Exceptions;

public class InvalidCredentialsException extends UnauthorizedException {
    @Override
    public String getMessage() {
        return "Invalid credentials";
    }
}
