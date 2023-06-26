package com.roarstead.Components.Exceptions;

import com.roarstead.Components.Response.Response;

public class NotFoundException extends HttpException {
    public NotFoundException() {
        super(Response.NOT_FOUND_MSG, Response.NOT_FOUND);
    }
}
