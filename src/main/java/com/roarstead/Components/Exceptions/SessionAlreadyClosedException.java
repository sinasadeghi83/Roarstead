package com.roarstead.Components.Exceptions;

public class SessionAlreadyClosedException extends RuntimeException{
    public SessionAlreadyClosedException() {
        super("Session is already close!!");
    }
}
