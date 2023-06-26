package com.roarstead.Components.Exceptions;

import com.roarstead.Components.Response.Response;

public class ConflictException extends HttpException {
    public ConflictException(String message) {
        super(message, Response.CONFLICT);
    }

    public ConflictException(){
        super(Response.CONFLICT_MSG, Response.CONFLICT);
    }
}
