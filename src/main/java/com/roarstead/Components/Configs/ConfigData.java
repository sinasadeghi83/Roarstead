package com.roarstead.Components.Configs;

import com.google.gson.annotations.SerializedName;

public class ConfigData {
    @SerializedName("secret_key_file_name")
    private String secretFileName;
    @SerializedName("jwt_expire_days")
    private int jwtExpireDays;
    @SerializedName("countries_file_name")
    private String countriesFileName;
    @SerializedName("date_format")
    private String dateFormat;

    public String getSecretFileName() {
        return secretFileName;
    }

    public void setSecretFileName(String secretFileName) {
        this.secretFileName = secretFileName;
    }

    public int getJwtExpireDays() {
        return jwtExpireDays;
    }

    public void setJwtExpireDays(int jwtExpireDays) {
        this.jwtExpireDays = jwtExpireDays;
    }

    public String getCountriesFileName() {
        return countriesFileName;
    }

    public void setCountriesFileName(String countriesFileName) {
        this.countriesFileName = countriesFileName;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }
}
