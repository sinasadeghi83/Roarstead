package com.roarstead;

import com.roarstead.Components.Auth.AuthManager;
import com.roarstead.Components.Auth.Rbac.RbacConfig;
import com.roarstead.Components.Configs.Config;
import com.roarstead.Components.Database.Database;
import com.roarstead.Components.Request.RequestHandler;
import com.roarstead.Components.Response.ResponseHandler;
import com.sun.net.httpserver.HttpExchange;

public class App extends Thread {
    private final HttpExchange httpExchange;
    private final AuthManager authManager;
    private final Database database;
    private final Config config;
    private final RbacConfig rbacConfig;

    public App(HttpExchange httpExchange) {
        super();
        this.httpExchange = httpExchange;
        this.database = new Database();
        this.config = new Config();
        this.rbacConfig = new com.roarstead.Configs.Auth.Rbac.RbacConfig();
        this.authManager = new AuthManager();
    }

    public static App getCurrentApp(){
        return (App) Thread.currentThread();
    }

    public HttpExchange getHttpExchange() {
        return this.httpExchange;
    }

    @Override
    public void run() {
        database.openSessionIfNotOpened();
        config.init();
        authManager.init();
        ResponseHandler responseHandler = new ResponseHandler();
        RequestHandler requestHandler = new RequestHandler(responseHandler);
        requestHandler.handle();
        database.closeSessionIfNotClosed();
    }

    public AuthManager getAuthManager() {
        return this.authManager;
    }

    public RbacConfig getRbacConfig() {
        return this.rbacConfig;
    }

    public Database getDb() {
        return this.database;
    }

    public Config getConfig() {
        return config;
    }
}
