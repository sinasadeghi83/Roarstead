package com.roarstead.Components.Request;

import com.roarstead.App;
import com.roarstead.Components.Configs.Config;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class HttpHandler implements com.sun.net.httpserver.HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        App app = new App(httpExchange);
        app.start();
    }
}
