package com.roarstead.Exception;

public class SessionIsClosedException extends RuntimeException{
    public SessionIsClosedException() {
        super("Session is already close!!");
    }
}
