package com.roarstead.Components.Response;

public class Response {
    public static final String NOT_FOUND = "404 Not found";
    public static final int INTERNAL_ERROR = 500;
    private String message;
    private int code;

    public Response(String message, int code) {
        this.message = message;
        this.code = code;
    }
    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
