package com.roarstead;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.roarstead.Components.Auth.AuthManager;
import com.roarstead.Components.Auth.Rbac.RbacConfig;
import com.roarstead.Components.Configs.Config;
import com.roarstead.Components.Database.Database;
import com.roarstead.Components.Request.RequestHandler;
import com.roarstead.Components.Response.ResponseHandler;
import com.roarstead.Components.Serializing.DateDeserializer;
import com.roarstead.Components.Serializing.ExcludeStrategy;
import com.sun.net.httpserver.HttpExchange;

import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

public class App extends Thread {
    private final HttpExchange httpExchange;
    private final AuthManager authManager;
    private final Database database;
    private final Config config;
    private final RbacConfig rbacConfig;
    private final Gson gson;
    private final ClassLoader classLoader;

    public App(HttpExchange httpExchange, ClassLoader classLoader) {
        super();
        this.httpExchange = httpExchange;
        this.database = new Database();
        this.config = new Config();
        this.rbacConfig = new com.roarstead.Configs.Auth.Rbac.RbacConfig();
        this.authManager = new AuthManager();
        gson = new GsonBuilder().setExclusionStrategies(new ExcludeStrategy()).registerTypeAdapter(Date.class, new DateDeserializer()).create();
        this.classLoader = classLoader;
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

    public Gson getGson() {
        return gson;
    }

    public Path getPath(String filePath) {
        try {
            URL url = classLoader.getResource(filePath);
            return Paths.get(url.toURI());
        } catch (URISyntaxException | NullPointerException e) {
            return null;
        }
    }
}
