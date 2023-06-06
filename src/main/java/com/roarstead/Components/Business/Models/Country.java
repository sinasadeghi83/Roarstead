package com.roarstead.Models;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.roarstead.App;
import jakarta.persistence.Embeddable;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Country {
    public Country(){}

    public Country(String name, String dialCode, String emoji, String code) {
        this.name = name;
        this.dialCode = dialCode;
        this.emoji = emoji;
        this.code = code;
    }

    private String name;

    @SerializedName("dial_code")
    private String dialCode;

    private String emoji;

    private String code;

    public boolean validate(){
        try (FileReader fileReader = new FileReader(App.getCurrentApp().getConfig().getCountriesFileName())){
            Type listCountryType = new TypeToken<ArrayList<Country>>(){}.getType();
            List<Country> countries = App.getCurrentApp().getGson().fromJson(fileReader, listCountryType);
            return countries.contains(this);
        } catch (IOException e) {
            return false;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDialCode() {
        return dialCode;
    }

    public void setDialCode(String dialCode) {
        this.dialCode = dialCode;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
