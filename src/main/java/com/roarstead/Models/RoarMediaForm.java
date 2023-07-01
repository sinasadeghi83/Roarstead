package com.roarstead.Models;

import com.google.gson.annotations.SerializedName;
import com.roarstead.Components.Resource.Models.Media;
import jakarta.persistence.*;

public class RoarMediaForm {
    protected int id;

    @SerializedName("media_type")
    @Enumerated(EnumType.STRING)
    protected RoarMedia.MediaType mediaType;

    @SerializedName("alt_text")
    protected String altText;

    @SerializedName("media_id")
    int mediaId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RoarMedia.MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(RoarMedia.MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    public int getMediaId() {
        return mediaId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }
}
