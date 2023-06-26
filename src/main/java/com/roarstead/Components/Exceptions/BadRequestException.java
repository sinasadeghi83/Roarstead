package com.roarstead.Components.Exceptions;

import com.roarstead.Components.Response.Response;

public class BadRequestException extends HttpException {
    public BadRequestException() {
        super(Response.BAD_REQUEST_MSG, Response.BAD_REQUEST);
    }
}
