package com.roarstead.Models;

import com.roarstead.Components.Resource.Models.Media;
import jakarta.persistence.*;

@Entity
@Table
public class RoarMedia {

    public enum MediaType{
        IMAGE,
        VIDEO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;

    @Enumerated(EnumType.STRING)
    protected MediaType mediaType;

    protected String altText;

    @OneToOne
    @JoinColumn(name = "media_id")
    Media media;

    @ManyToOne
    @JoinColumn(name = "groar_id", nullable = false)
    GRoar groar;

    @ManyToOne
    @JoinColumn(name = "uploader_id", nullable = false)
    User uploader;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public GRoar getGroar() {
        return groar;
    }

    public void setGroar(GRoar groar) {
        this.groar = groar;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }
}
