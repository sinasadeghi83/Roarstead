package com.roarstead.Exception;

public class SessionAlreadyOpenedException extends RuntimeException {
    public SessionAlreadyOpenedException() {
        super("Session in already open!!");
    }
}
