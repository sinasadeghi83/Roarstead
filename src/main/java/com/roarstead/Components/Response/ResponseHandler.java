package com.roarstead.Components.Response;

import com.google.gson.Gson;
import com.roarstead.App;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public class ResponseHandler {
    public static final String JSON_CONTENT = "application/json";
    public static final String PNG_CONTENT = "image/png";
    public static final String JPEG_CONTENT = "image/jpeg";

    public void respond(Response response) {
        try {
            String contentType = response.getContentType();
            HttpExchange exchange = App.getCurrentApp().getHttpExchange();
            String rawResponse = "";
            switch (contentType){
                case JSON_CONTENT -> rawResponse = App.getCurrentApp().getGson().toJson(response);
                //Send media as base64
                case PNG_CONTENT -> rawResponse = (String) response.getMessage();
                case JPEG_CONTENT -> rawResponse = (String) response.getMessage();
            }
            byte[] bytesResponse = rawResponse.getBytes();
            exchange.getResponseHeaders().set("Content-Type", contentType);
            exchange.sendResponseHeaders(response.getCode(), bytesResponse.length);

            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(bytesResponse);
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
