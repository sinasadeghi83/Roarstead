package com.roarstead.Components.Response;

public class Response {
    public static final String NOT_FOUND_MSG = "404 Not found";
    public static final String INTERNAL_ERROR_MSG = "500 Server internal error. Contact the server admin.";
    public static final String UNPROCESSABLE_ENTITY_MSG = "422 Unprocessable Entity";
    public static final String CONFLICT_MSG = "409 Conflict";
    public static final String BAD_REQUEST_MSG = "400 Bad Request";
    public static final String METHOD_NOT_ALLOWED_MSG = "405 Method Not Allowed";
    public static final String UNAUTHORIZED_MSG = "401 Unauthorized";
    public static final String OK_UPLOAD = "File(s) uploaded successfully";
    public static final String REQUEST_ENTITY_TOO_LARGE_MSG = "Uploaded file size must be less than %d %s!";
    public static final String FORBIDDEN_MSG = "403 forbidden";
    public static final int UNAUTHORIZED = 401;
    public static final int NOT_FOUND = 404;
    public static final int INTERNAL_ERROR = 500;
    public static final int OK = 200;
    public static final int UNPROCESSABLE_ENTITY = 422;
    public static final int CONFLICT = 409;
    public static final int BAD_REQUEST = 400;
    public static final int METHOD_NOT_ALLOWED = 405;
    public static final int REQUEST_ENTITY_TOO_LARGE = 413;
    public static final int FORBIDDEN = 403;


    private Object message;
    private int code;
    private String contentType;

    public Response(Object message, int code) {
        this.message = message;
        this.code = code;
        contentType = ResponseHandler.JSON_CONTENT;
    }

    public Response(Object message, int code, String contentType) {
        this.message = message;
        this.code = code;
        this.contentType = contentType;
    }

    public Response(Object message, String contentType) {
        this.message = message;
        this.code = Response.OK;
        this.contentType = contentType;
    }

    public Object getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
