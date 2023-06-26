package com.roarstead.Components.Exceptions;

import com.roarstead.Components.Response.Response;

public class UnprocessableEntityException extends HttpException {

    public UnprocessableEntityException() {
        super(Response.UNPROCESSABLE_ENTITY_MSG, Response.UNPROCESSABLE_ENTITY);
    }

    public UnprocessableEntityException(String message) {
        super(message, Response.UNPROCESSABLE_ENTITY);
    }
}
