package com.roarstead.Components.Exceptions;

public class SessionAlreadyOpenedException extends RuntimeException {
    public SessionAlreadyOpenedException() {
        super("Session in already open!!");
    }
}
