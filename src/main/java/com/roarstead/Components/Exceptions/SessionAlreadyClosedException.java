package com.roarstead.Components.Exceptions;

public class SessionAlreadyClosedException extends Exception{
    public SessionAlreadyClosedException() {
        super("Session is already close!!");
    }
}
