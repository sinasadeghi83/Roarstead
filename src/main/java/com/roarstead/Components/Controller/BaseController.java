package com.roarstead.Components.Controller;

import com.google.gson.Gson;
import com.roarstead.App;
import com.roarstead.Components.Response.Response;
import com.roarstead.Components.Response.ResponseHandler;
import com.roarstead.Models.User;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public abstract class BaseController {

    public abstract Map<String, List<String>> accessControl();
    public void init(){

    }
    public BaseController(){
        init();
    }

    public Response runAction(String action, String rawRequestBody){
        //TODO handle request body
        System.out.println("Request body:\n" + rawRequestBody);
        try {
            //TODO Check user access control
//            User user = new User();
//            user.setId(App.getCurrentApp().getArgs().get(0));
//            user.setPassword(App.getArgs().get(1));
//            App.getAuthManager().authenticate(user);
//            checkAccessControl(action);
        } catch (Exception e) {
            //TODO handle exceptions
//            return new Response(e.getMessage(), );
        }
        //TODO handle method
        Method method = null;
//        try {
//            method = this.getClass().getMethod(action, List.class);
//            return (Response) method.invoke(this, App.getArgs().subList(2, App.getArgs().size()));
//        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
//            throw new RuntimeException(e);
//        }
        //TODO: This line should be deleted after completion of all todos
        return new Response("Server is not ready", Response.INTERNAL_ERROR);
    }

    //TODO handle checkAccessControl
//    private void checkAccessControl(String action) throws NotAuthenticatedException, NotAuthorisedException {
//        Map<String, List<String>> access = accessControl();
//        List<String> rolesPerms = access.get(action);
//        if(!App.getAuthManager().authorise(rolesPerms)){
//            throw new NotAuthorisedException();
//        }
//    }
}

