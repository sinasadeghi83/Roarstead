package com.roarstead.Models;

import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.Size;

import java.util.Set;

public class GRoarForm extends RoarForm{

    @Size(max = GRoar.MAX_CHARS_GROAR)
    protected String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
