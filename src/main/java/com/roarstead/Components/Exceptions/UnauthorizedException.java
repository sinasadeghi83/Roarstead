package com.roarstead.Components.Exceptions;

import com.roarstead.Components.Response.Response;

public class UnauthorizedException extends HttpException{
    public UnauthorizedException(){
        super(Response.UNAUTHORIZED_MSG, Response.UNAUTHORIZED);
    }
}
