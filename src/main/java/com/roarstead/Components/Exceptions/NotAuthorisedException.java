package com.roarstead.Components.Exceptions;

public class NotAuthorisedException extends UnauthorizedException {
    @Override
    public String getMessage() {
        return "permission-denied";
    }
}
