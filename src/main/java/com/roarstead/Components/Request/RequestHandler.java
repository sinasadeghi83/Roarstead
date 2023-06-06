package com.roarstead.Components.Request;

import com.google.gson.JsonObject;
import com.roarstead.App;
import com.roarstead.Components.Annotation.POST;
import com.roarstead.Components.Controller.BaseController;
import com.roarstead.Components.Exceptions.MethodNotAllowedException;
import com.roarstead.Components.Response.Response;
import com.roarstead.Components.Response.ResponseHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.InputStream;
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
        Response response = null;
        try {
            String rawBody = new String(requestStream.readAllBytes(), StandardCharsets.UTF_8);
            requestStream.close();

            Class<? extends BaseController> controller = (Class<? extends BaseController>) Class.forName("com.roarstead.Controllers."+controllerName);
            System.out.println("Request Method: " + exchange.getRequestMethod());
            System.out.println("Annotations by type: " + controller.getMethod(actionName, JsonObject.class).getDeclaredAnnotationsByType(POST.class));
            System.out.println("Annotations: " + controller.getMethod(actionName, JsonObject.class).getAnnotations());
            if(exchange.getRequestMethod().equalsIgnoreCase("GET")
                    && controller.getMethod(actionName, JsonObject.class).getDeclaredAnnotationsByType(POST.class) != null){
                throw new MethodNotAllowedException();
            }
            BaseController baseController = controller.getDeclaredConstructor().newInstance();
            response = baseController.runAction(actionName, rawBody);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
            response = new Response(Response.NOT_FOUND_MSG, Response.NOT_FOUND);
        }catch (MethodNotAllowedException e){
            response = new Response(Response.METHOD_NOT_ALLOWED_MSG, Response.METHOD_NOT_ALLOWED);
        }catch (NullPointerException e) {
            response = new Response(Response.BAD_REQUEST_MSG, Response.BAD_REQUEST);
        }catch (Exception e) {
            if(e.getCause() != null){
                e = (Exception) e.getCause();
            }
            System.err.println(e.getMessage());
            e.printStackTrace();
            response = new Response(Response.INTERNAL_ERROR_MSG, Response.INTERNAL_ERROR);
        }
        responseHandler.respond(response, ResponseHandler.JSON_CONTENT);
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
