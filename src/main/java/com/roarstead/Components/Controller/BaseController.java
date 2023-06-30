package com.roarstead.Components.Controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.roarstead.App;
import com.roarstead.Components.Annotation.POST;
import com.roarstead.Components.Exceptions.*;
import com.roarstead.Components.Request.Request;
import com.roarstead.Components.Response.Response;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public abstract class BaseController {

    protected Gson gson;

    public abstract Map<String, List<String>> accessControl();
    public void init(){
    }
    public BaseController(){
        init();
        gson = App.getCurrentApp().getGson();
    }

    public Response runAction(String action, String rawRequestBody) throws Exception {
        HttpExchange exchange = App.getCurrentApp().getHttpExchange();
        Headers headers = exchange.getRequestHeaders();
        JsonObject jsonBody = null;
        if(rawRequestBody != null)
            jsonBody = gson.fromJson(rawRequestBody, JsonObject.class);
        if(headers.get(Request.AUTH_KEY) != null){
            String token = headers.get(Request.AUTH_KEY).toString();
            token = token.split(Request.SCHEME + " ")[1];
            try {
                App.getCurrentApp().getAuthManager().authenticateByJWT(token);
            } catch (AuthNotFoundException e) {
                throw new InvalidCredentialsException();
            }
        }
        checkAccessControl(action);
        Method method = null;
        try {
            if(jsonBody != null) {
                method = this.getClass().getMethod(action, JsonObject.class);
                if (exchange.getRequestMethod().equalsIgnoreCase("GET")
                        && method.getDeclaredAnnotationsByType(POST.class).length > 0) {
                    throw new MethodNotAllowedException();
                }
                return (Response) method.invoke(this, jsonBody);
            }else {
                method = this.getClass().getMethod(action);
                if (exchange.getRequestMethod().equalsIgnoreCase("GET")
                        && method.getDeclaredAnnotationsByType(POST.class).length > 0) {
                    throw new MethodNotAllowedException();
                }
                return (Response) method.invoke(this);
            }
        }catch (Exception e){
            if(e.getCause() != null)
                throw (Exception) e.getCause();
            throw e;
        }
    }

    private void checkAccessControl(String action) throws UnauthorizedException {
        Map<String, List<String>> access = this.accessControl();
        List<String> rolesPerms = access.get(action);
        if(!App.getCurrentApp().getAuthManager().authorise(rolesPerms)){
            throw new UnauthorizedException();
        }
    }
}

