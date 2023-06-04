package com.roarstead.Components.Exceptions;

public class AuthNotFoundException extends UnauthorizedException {
    @Override
    public String getMessage() {
        return "not-found";
    }
}
