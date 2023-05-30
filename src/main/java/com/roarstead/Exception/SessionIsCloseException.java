package com.roarstead.Exception;

public class SessionIsCloseException extends RuntimeException{
    public SessionIsCloseException() {
        super("Session is already close!!");
    }
}
