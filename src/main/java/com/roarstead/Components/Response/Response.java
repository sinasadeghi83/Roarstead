package com.roarstead.Components.Response;

public class Response {
    public static final String NOT_FOUND_MSG = "404 Not found";
    public static final String INTERNAL_ERROR_MSG = "500 Server internal error. Contact the server admin.";
    public static final int NOT_FOUND = 404;
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
