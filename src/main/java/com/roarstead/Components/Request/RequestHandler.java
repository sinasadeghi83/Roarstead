package com.roarstead.Components.Request;

import com.roarstead.App;
import com.roarstead.Components.Controller.BaseController;
import com.roarstead.Components.Response.Response;
import com.roarstead.Components.Response.ResponseHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

public class RequestHandler {

    private ResponseHandler responseHandler;

    public RequestHandler(ResponseHandler responseHandler) {
        this.responseHandler = responseHandler;
    }

    public void handle() {
        HttpExchange exchange = App.getCurrentApp().getHttpExchange();

        String requestPath = exchange.getRequestURI().getPath();
        String[] pathParts = requestPath.split("/");
        String controllerName = pathParts[1];
        String actionName = pathParts.length > 2 ? pathParts[2] : "index";
        /*
        Example 1: http://localhost:PORT/test/index
            actionName: action-index -> actionIndex
            controllerName: TestController
        ----------------------------------------------
        Example 2: http://localhost:PORT/my-user/add-user user -> UserController
            actionName: action-add-user -> actionAddUser
            controllerName: my-user -> MyUserController
         */
        actionName = parseRouteWord("action-"+actionName);
        controllerName = controllerName.substring(0, 1).toUpperCase() + parseRouteWord(controllerName.substring(1)) + "Controller";

        InputStream requestStream = exchange.getRequestBody();
        try {
            String rawBody = new String(requestStream.readAllBytes(), StandardCharsets.UTF_8);
            requestStream.close();

            Class<? extends BaseController> controller = (Class<? extends BaseController>) Class.forName("com.roarstead.Controllers."+controllerName);
            BaseController baseController = controller.getDeclaredConstructor().newInstance();
            Response response = baseController.runAction(actionName, rawBody);
            responseHandler.respond(response, ResponseHandler.JSON_CONTENT);
        } catch (ClassNotFoundException e) {
            handleNotFound();
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleNotFound() {
        Response notFound = new Response(Response.NOT_FOUND, 404);
        responseHandler.respond(notFound, ResponseHandler.JSON_CONTENT);
    }

    private String parseRouteWord(String routeWord) {
        StringBuilder builder = new StringBuilder(routeWord);
        int index;
        while ((index = builder.indexOf("-")) != -1){
            builder.deleteCharAt(index);
            builder.setCharAt(index, Character.toUpperCase(builder.charAt(index)));
        }
        return builder.toString();
    }
}
