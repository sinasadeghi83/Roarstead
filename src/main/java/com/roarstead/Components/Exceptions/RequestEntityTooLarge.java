package com.roarstead.Components.Exceptions;

import com.roarstead.Components.Response.Response;

public class RequestEntityTooLarge extends HttpException {
    public RequestEntityTooLarge(long maxSize, String unit) {
        super(String.format(Response.REQUEST_ENTITY_TOO_LARGE_MSG, maxSize, unit), Response.REQUEST_ENTITY_TOO_LARGE);
    }
}
