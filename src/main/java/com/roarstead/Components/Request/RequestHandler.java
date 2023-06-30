package com.roarstead.Components.Request;

import com.google.gson.JsonObject;
import com.roarstead.App;
import com.roarstead.Components.Annotation.POST;
import com.roarstead.Components.Controller.BaseController;
import com.roarstead.Components.Exceptions.BadRequestException;
import com.roarstead.Components.Exceptions.HttpException;
import com.roarstead.Components.Exceptions.MethodNotAllowedException;
import com.roarstead.Components.Exceptions.NotFoundException;
import com.roarstead.Components.Response.Response;
import com.roarstead.Components.Response.ResponseHandler;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RequestHandler {

    private ResponseHandler responseHandler;

    public RequestHandler(ResponseHandler responseHandler) {
        this.responseHandler = responseHandler;
    }

    public void handle() {
        HttpExchange exchange = App.getCurrentApp().getHttpExchange();

        String requestPath = exchange.getRequestURI().getPath();
//        requestPath = requestPath.substring(0, requestPath.indexOf("?"));
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
        Response response = null;
        //Handles HttpException to return proper response
        //Otherwise logging the caused exception and returns internal error response
        try {
            Headers headers = exchange.getRequestHeaders();
            String contentType = ((headers.get("Content-type") != null) ? (headers.get("Content-type").toString()) : (null));
            long contentLength = ((headers.get("Content-length") != null) ? Long.parseLong(headers.get("Content-length").get(0)) : (0));
            String rawBody = null;
            if(contentType != null && contentType.contains("multipart/form-data")) {
                App.getCurrentApp().getResourceManager().retrieveDownloadedFiles();
            }else if(contentLength > 0){
                rawBody = new String(requestStream.readAllBytes(), StandardCharsets.UTF_8);
                requestStream.close();
            }
            //Convert three specific not found related exceptions to their according HttpException
            try {
                Class<? extends BaseController> controller = (Class<? extends BaseController>) Class.forName("com.roarstead.Controllers." + controllerName);
                BaseController baseController = controller.getDeclaredConstructor().newInstance();
                response = baseController.runAction(actionName, rawBody);
            }catch (ClassNotFoundException | NoSuchMethodException e) {
                throw new NotFoundException();
            }catch (NullPointerException e) {
                e.printStackTrace();
                System.err.println(e.getMessage());
                throw new BadRequestException();
            }
        } catch (HttpException e){
            response = new Response(e.getMessage(), e.getCode());
        }catch (Exception e) {
            Exception causeException = e;
            if(causeException.getCause() != null){
                causeException = (Exception) causeException.getCause();
            }
            System.err.println(causeException.getMessage());
            causeException.printStackTrace();
            response = new Response(Response.INTERNAL_ERROR_MSG, Response.INTERNAL_ERROR);
        }
        responseHandler.respond(response, ResponseHandler.JSON_CONTENT);
    }

    //Parse routes to camelCase format
    private String parseRouteWord(String routeWord) {
        StringBuilder builder = new StringBuilder(routeWord);
        int index;
        while ((index = builder.indexOf("-")) != -1){
            builder.deleteCharAt(index);
            builder.setCharAt(index, Character.toUpperCase(builder.charAt(index)));
        }
        return builder.toString();
    }

    public static Map<String, String> queryToMap(String query) {
        if(query == null) {
            return null;
        }
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            }else{
                result.put(entry[0], "");
            }
        }
        return result;
    }
}
