package com.roarstead.Exception;

public class SessionAlreadyClosedException extends RuntimeException{
    public SessionAlreadyClosedException() {
        super("Session is already close!!");
    }
}
