package com.roarstead.Models;

import com.roarstead.App;
import com.roarstead.Components.Annotation.Exclude;
import com.roarstead.Components.Exceptions.NotFoundException;
import com.roarstead.Components.Exceptions.RequestEntityTooLarge;
import com.roarstead.Components.Exceptions.UnprocessableEntityException;
import com.roarstead.Components.Resource.Models.Image;
import com.roarstead.Components.Resource.Models.Media;
import jakarta.persistence.*;

@Entity
@Table
public class RoarMedia {

    public static final long MAX_ROAR_IMAGE_SIZE = 2 * 1024; //in kilobytes
    public static final int MAX_WIDTH_ROAR_MEDIA = 900; //in px
    public static final int MAX_HEIGHT_ROAR_MEDIA = 1600; //in px
    public static final String ROAR_MEDIA_DIM_ERR = "Media dimension is invalid!";

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

    @Exclude
    @OneToOne
    @JoinColumn(name = "media_id", nullable = false)
    Media media;

    @ManyToOne
    @JoinColumn(name = "groar_id", nullable = false)
    GRoar groar;

    @Exclude
    @ManyToOne
    @JoinColumn(name = "uploader_id", nullable = false)
    User uploader;

    public RoarMedia(){}

    public RoarMedia(MediaType mediaType, String altText, User uploader, GRoar roar) {
        this.mediaType = mediaType;
        this.altText = altText;
        this.uploader = uploader;
        this.groar = roar;
    }

    public void loadMediaWithImage(Image image) throws Exception {
        validateImage(image);
        this.media = image;
    }

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

    public User getUploader() {
        return uploader;
    }

    public void setUploader(User uploader) {
        this.uploader = uploader;
    }

    public static void validateImage(Image image) throws Exception{
        if(image.mediaSize() > MAX_ROAR_IMAGE_SIZE)
            throw new RequestEntityTooLarge(MAX_ROAR_IMAGE_SIZE, Image.SIZE_UNIT);
        int height = image.getHeight();
        int width = image.getWidth();
        if(width > MAX_WIDTH_ROAR_MEDIA || height > MAX_HEIGHT_ROAR_MEDIA)
            throw new UnprocessableEntityException(ROAR_MEDIA_DIM_ERR);
    }
}
