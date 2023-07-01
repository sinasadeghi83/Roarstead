package com.roarstead.Components.Exceptions;

import com.roarstead.Components.Response.Response;

public class ForbiddenException extends HttpException {
    public ForbiddenException() {
        super(Response.FORBIDDEN_MSG, Response.FORBIDDEN);
    }

    public ForbiddenException(String message) {
        super(message, Response.FORBIDDEN);
    }
}
