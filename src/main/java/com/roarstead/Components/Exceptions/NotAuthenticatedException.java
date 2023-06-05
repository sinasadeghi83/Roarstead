package com.roarstead.Components.Exceptions;

public class NotAuthenticatedException extends UnauthorizedException {
    @Override
    public String getMessage() {
        return "permission-denied";
    }
}
