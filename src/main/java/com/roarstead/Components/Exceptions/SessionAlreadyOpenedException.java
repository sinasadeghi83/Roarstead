package com.roarstead.Components.Exceptions;

public class SessionAlreadyOpenedException extends Exception {
    public SessionAlreadyOpenedException() {
        super("Session in already open!!");
    }
}
