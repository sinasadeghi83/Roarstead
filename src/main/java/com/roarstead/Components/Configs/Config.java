package com.roarstead.Components.Configs;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

public class Config {
    public static final String SECRET_FILE_NAME = "secret_key.json";
    public static final long JWT_EXPIRATION_TIME = 30L * 86400000; // 86400000 : 24 hours in milliseconds

    private SecretKey secretKey = null;
    private final SignatureAlgorithm secretAlg = SignatureAlgorithm.HS256;

    public void init(){
        secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256); //or HS384 or HS512
        if(loadSecretKey()) {
            generateSecretKey();
            saveSecretKey();
        }
    }

    private void generateSecretKey() {
        secretKey = Keys.secretKeyFor(secretAlg);
    }

    private boolean loadSecretKey() {
        try {
            Path secretPath = Paths.get(SECRET_FILE_NAME);
            if(!Files.exists(secretPath)){
                return false;
            }
            FileInputStream fileInput = new FileInputStream(secretPath.toFile());
            byte[] keyBytes = new byte[32];
            fileInput.read(keyBytes);
            secretKey = Keys.hmacShaKeyFor(keyBytes);;
            return true;
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public void saveSecretKey(){
        try {
            Path secretPath = Paths.get(SECRET_FILE_NAME);
            if(!Files.exists(secretPath)){
                Files.createFile(secretPath);
            }

            FileOutputStream fileOut = new FileOutputStream(secretPath.toFile());
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();

            byte[] keyBytes = secretKey.getEncoded();
            byteOut.write(keyBytes);
            byteOut.writeTo(fileOut);

            byteOut.close();
            fileOut.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }

    public SignatureAlgorithm getSecretAlg() {
        return secretAlg;
    }
}
