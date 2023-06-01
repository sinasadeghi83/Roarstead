package com.roarstead;

import com.roarstead.Components.Request.RequestHandler;
import com.roarstead.Components.Response.ResponseHandler;
import com.sun.net.httpserver.HttpExchange;

public class App extends Thread {
    private final HttpExchange httpExchange;

    public App(HttpExchange httpExchange) {
        this.httpExchange = httpExchange;
    }

    public static App getCurrentApp(){
        return (App) Thread.currentThread();
    }

    public HttpExchange getHttpExchange() {
        return this.httpExchange;
    }

    @Override
    public void run() {
        ResponseHandler responseHandler = new ResponseHandler();
        RequestHandler requestHandler = new RequestHandler(responseHandler);
        requestHandler.handle();
    }
}
