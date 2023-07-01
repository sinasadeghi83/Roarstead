package com.roarstead.Models;

import jakarta.validation.constraints.Size;

public class GRoarForm extends RoarForm{

    @Size(min = GRoar.MIN_CHARS_GROAR, max = GRoar.MAX_CHARS_GROAR)
    protected String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
