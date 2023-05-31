package com.roarstead;

import com.roarstead.Components.Request.HttpHandler;
import com.roarstead.Components.Request.RequestHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpListener {
    private static final int HTTP_PORT = 8000;
    public static void main( String[] args ) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(HTTP_PORT), 0);
            server.createContext("/", new HttpHandler());
            server.start();
            System.out.println("Server started on port " + HTTP_PORT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
