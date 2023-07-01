package com.roarstead.Models;

import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.Size;

public class GRoarForm extends RoarForm{

    @Size(min = GRoar.MIN_CHARS_GROAR, max = GRoar.MAX_CHARS_GROAR)
    protected String text;

    @SerializedName("reply_to")
    protected int replyTo;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(int replyTo) {
        this.replyTo = replyTo;
    }
}
