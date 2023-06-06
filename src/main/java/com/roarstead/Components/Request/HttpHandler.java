package com.roarstead.Components.Request;

import com.roarstead.App;
import com.roarstead.Components.Configs.Config;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class HttpHandler implements com.sun.net.httpserver.HttpHandler {

    private ClassLoader classLoader;
    public HttpHandler(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        App app = new App(httpExchange, classLoader);
        app.start();
    }
}
