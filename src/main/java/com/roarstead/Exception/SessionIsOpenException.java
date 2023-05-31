package com.roarstead.Exception;

public class SessionIsOpenException extends RuntimeException {
    public SessionIsOpenException() {
        super("Session in already open!!");
    }
}
