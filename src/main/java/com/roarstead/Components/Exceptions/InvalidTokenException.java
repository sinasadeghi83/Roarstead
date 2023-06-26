package com.roarstead.Components.Exceptions;

import com.roarstead.Components.Response.Response;

public class InvalidTokenException extends UnauthorizedException {
    @Override
    public String getMessage() {
        return "Invalid token";
    }
}
