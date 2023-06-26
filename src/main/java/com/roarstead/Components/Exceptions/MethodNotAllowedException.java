package com.roarstead.Components.Exceptions;

import com.roarstead.Components.Response.Response;

public class MethodNotAllowedException extends HttpException {
    public MethodNotAllowedException(){
        super(Response.METHOD_NOT_ALLOWED_MSG, Response.METHOD_NOT_ALLOWED);
    }
}
