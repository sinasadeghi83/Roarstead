package com.roarstead.Components.Exceptions;

public class HttpException extends Exception{
    protected int code;

    public HttpException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
