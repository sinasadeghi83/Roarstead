package com.roarstead.Components.Response;

import com.google.gson.Gson;
import com.roarstead.App;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public class ResponseHandler {
    public static final String JSON_CONTENT = "application/json";

    public void respond(Response response, String contentType) {
        try {
            HttpExchange exchange = App.getCurrentApp().getHttpExchange();
            String rawResponse = "";
            switch (contentType){
                case JSON_CONTENT -> rawResponse = new Gson().toJson(response);
            }
            exchange.getResponseHeaders().set("Content-Type", contentType);
            exchange.sendResponseHeaders(response.getCode(), rawResponse.length());

            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(rawResponse.getBytes());
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
