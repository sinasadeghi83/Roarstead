package com.roarstead.Components.Auth.Models;

import com.roarstead.Components.Exceptions.InvalidPasswordException;
import com.roarstead.Components.Exceptions.ModelNotFoundException;
import com.roarstead.Components.Exceptions.NotAuthenticatedException;

public interface Auth {

    public int getId();
    public void setId(int id);
    public void enterPassword(String password);
    public String getPassword();
    public Auth identity() throws NotAuthenticatedException;
    public Auth authenticate() throws InvalidPasswordException, ModelNotFoundException;
}