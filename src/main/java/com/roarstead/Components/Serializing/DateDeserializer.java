package com.roarstead.Components.Serializing;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateDeserializer implements JsonDeserializer<Date> {
    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dateStr = json.getAsString();
        try {
            return formatter.parse(dateStr);
        } catch (ParseException e) {
            throw new JsonParseException(e);
        }
    }
}
