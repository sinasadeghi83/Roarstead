package com.roarstead.Components.Configs;

import com.google.gson.Gson;
import com.roarstead.App;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class Config {
    public final static String CONFIG_FILE_NAME = "config.json";
    public Path secretPath;
    public long jwtExpireTime = 86400000; // 86400000 : 24 hours in milliseconds
    public Path countriesPath;
    public String dateFormat = "EEE, dd MMM yyyy HH:mm:ss zzz";

    private SecretKey secretKey = null;
    private final SignatureAlgorithm secretAlg = SignatureAlgorithm.HS256;

    private ConfigData configData = null;

    public void init(){
        loadConfigData();
        if(!loadSecretKey()) {
            generateSecretKey();
            saveSecretKey();
        }
        System.out.println("\n\nCurrent Secret Key:\n" + new String(secretKey.getEncoded()) +"\n\n");
    }

    private void loadConfigData() {
        try{
            Path configPath = App.getCurrentApp().getPath(CONFIG_FILE_NAME);
            FileReader fileReader = new FileReader(configPath.toFile());
            configData = new Gson().fromJson(fileReader, ConfigData.class);
            jwtExpireTime *= configData.getJwtExpireDays(); //JWT Expire time in config is in days unit
            countriesPath = App.getCurrentApp().getPath(configData.getCountriesFileName());
            secretPath = App.getCurrentApp().getPath(configData.getSecretFileName());
            dateFormat = configData.getDateFormat();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void generateSecretKey() {
        secretKey = Keys.secretKeyFor(secretAlg);
    }

    private boolean loadSecretKey() {
        try {
            if(secretPath == null){
                return false;
            }
            FileInputStream fileInput = new FileInputStream(secretPath.toFile());
            byte[] keyBytes = new byte[32];
            fileInput.read(keyBytes);
            secretKey = Keys.hmacShaKeyFor(keyBytes);;
            return true;
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("IOException at Config loadSecretKey: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void saveSecretKey(){
        try {
            File secretFile = new File(configData.getSecretFileName());
            if(secretPath == null || !Files.exists(secretPath)){
                secretFile.createNewFile();
                secretPath = Path.of(secretFile.getPath());
            }else{
                secretFile = secretPath.toFile();
            }

            FileOutputStream fileOut = new FileOutputStream(secretFile);
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

    public long getJwtExpireTime() {
        return jwtExpireTime;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public Path getSecretPath() {
        return secretPath;
    }

    public Path getCountriesPath() {
        return countriesPath;
    }
}
