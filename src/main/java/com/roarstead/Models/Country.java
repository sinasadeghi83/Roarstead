package com.roarstead.Models;

import com.google.gson.annotations.SerializedName;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;

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
