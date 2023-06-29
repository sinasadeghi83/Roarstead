package com.roarstead;

import com.roarstead.Components.Auth.Models.Role;
import com.roarstead.Components.Configs.Config;
import com.roarstead.Components.Database.Database;
import com.roarstead.Components.Request.HttpHandler;
import com.roarstead.Components.Request.RequestHandler;
import com.sun.net.httpserver.HttpServer;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class HttpListener {
    private static final int HTTP_PORT = 8000;
    public static void main( String[] args ) {
        try {
            initialConfigs();
            HttpServer server = HttpServer.create(new InetSocketAddress(HTTP_PORT), 0);
            server.createContext("/", new HttpHandler(HttpListener.class.getClassLoader()));
            server.start();
            System.out.println("Server started on port " + HTTP_PORT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Creates the default role "@"
    private static void initialConfigs() {
        Database db = new Database();
        db.openSessionIfNotOpened();
        long defaultRoleCount = (Long) db.getSession()
                .createQuery("SELECT count(r) FROM Role r WHERE r.name=:name")
                .setParameter("name", Role.DEFAULT_NAME)
                .getSingleResult();
        if(defaultRoleCount == 0) {
            db.ready();
            Role role = new Role();
            role.setName(Role.DEFAULT_NAME);
            db.getSession().persist(role);
            db.done();
        }
        db.closeSessionIfNotClosed();
    }
}
