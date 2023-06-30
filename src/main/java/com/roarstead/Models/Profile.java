package com.roarstead.Models;

import com.google.gson.JsonObject;
import com.roarstead.App;
import com.roarstead.Components.Annotation.Exclude;
import com.roarstead.Components.Database.Database;
import com.roarstead.Components.Exceptions.RequestEntityTooLarge;
import com.roarstead.Components.Exceptions.UnprocessableEntityException;
import com.roarstead.Components.Resource.Models.Image;
import com.roarstead.Components.Resource.ResourceManager;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
@Embeddable
public class Profile {
    public static final int MAX_HEADER_SIZE = 2048; //in Kilobytes
    public static final int MAX_AVATAR_SIZE = 1024; //in Kilobytes
    public static final int HEIGHT_AVATAR = 400;
    public static final int WIDTH_AVATAR = 400;
    public static final int WIDTH_HEADER = 1500;
    public static final int HEIGHT_HEADER = 500;
    public static final String AVATAR_SIZE_ERROR = "Profile image size must be 400px * 400px";
    public static final String HEADER_SIZE_ERROR = "Profile image size must be 1500px * 500px";

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "prof_img_id", referencedColumnName = "id")
    @Exclude
    private Image profImage;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="header_img_id", referencedColumnName = "id")
    @Exclude
    private Image headerImage;

    @Column(name = "bio")
    @Size(min = 0,max = 160,message = "bio must be between 0 and 160 characters")
    private String bio;

    @Column
    @NotNull(message = "location can not be NULL")
    private String location;

    @Column(name = "url")
    private URL websiteLink;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", insertable = true, updatable = false)
    private Date createdAt;

    public static void validateSizeForHeaderImage(int fileItemIndex) throws RequestEntityTooLarge {
        if(App.getCurrentApp().getResourceManager().getFileItemSize(fileItemIndex) > MAX_HEADER_SIZE)
            throw new RequestEntityTooLarge(MAX_HEADER_SIZE, Image.SIZE_UNIT);
    }

    public static void validateForHeaderImage(Image headerImage) throws IOException, RequestEntityTooLarge, UnprocessableEntityException {
        if(headerImage.imageSize() > MAX_HEADER_SIZE)
            throw new RequestEntityTooLarge(MAX_HEADER_SIZE, Image.SIZE_UNIT);
        int height = headerImage.getHeight();
        int width = headerImage.getWidth();
        if(width != WIDTH_HEADER || height != HEIGHT_HEADER)
            throw new UnprocessableEntityException(HEADER_SIZE_ERROR);
    }

    public static void validateSizeForAvatarImage(int fileItemIndex) throws RequestEntityTooLarge {
        if(App.getCurrentApp().getResourceManager().getFileItemSize(fileItemIndex) > MAX_AVATAR_SIZE)
            throw new RequestEntityTooLarge(MAX_AVATAR_SIZE, Image.SIZE_UNIT);
    }

    public static void validateForAvatarImage(Image profImage) throws IOException, RequestEntityTooLarge, UnprocessableEntityException {
        if(profImage.imageSize() > MAX_AVATAR_SIZE)
            throw new RequestEntityTooLarge(MAX_AVATAR_SIZE, Image.SIZE_UNIT);
        int height = profImage.getHeight();
        int width = profImage.getWidth();
        if(width != WIDTH_AVATAR || height != HEIGHT_AVATAR)
            throw new UnprocessableEntityException(AVATAR_SIZE_ERROR);
    }

    public Image getProfImage() {
        return profImage;
    }

    public void setProfImage(Image profImage) throws RequestEntityTooLarge, IOException, UnprocessableEntityException {
        validateForAvatarImage(profImage);
        this.profImage = profImage;
    }

    public Image getHeaderImage() {
        return headerImage;
    }

    public void setHeaderImage(Image headerImage) throws RequestEntityTooLarge, IOException, UnprocessableEntityException {
        validateForHeaderImage(headerImage);
        this.headerImage = headerImage;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public URL getWebsiteLink() {
        return websiteLink;
    }

    public void setWebsiteLink(URL websiteLink) {
        this.websiteLink = websiteLink;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void deleteProfileImage(User user) {
        if(this.profImage == null)
            return;
        Database db = App.getCurrentApp().getDb();
        Image profImageTemp = this.profImage;
        db.ready();
        this.profImage = null;
        db.getSession().update(user);
        db.done();
        ResourceManager resourceManager = App.getCurrentApp().getResourceManager();
        resourceManager.deleteImage(profImageTemp);
    }

    public void deleteHeaderImage(User user) {
        if(this.headerImage == null)
            return;
        Database db = App.getCurrentApp().getDb();
        Image headerImage = this.headerImage;
        db.ready();
        this.headerImage = null;
        db.getSession().merge(user);
        db.done();
        ResourceManager resourceManager = App.getCurrentApp().getResourceManager();
        resourceManager.deleteImage(headerImage);
    }

    public JsonObject toJson(){
        JsonObject result = new JsonObject();
        result.addProperty("bio", bio);
        result.addProperty("location", location);
        result.addProperty("url", websiteLink != null ? websiteLink.toString() : null);
        result.addProperty("created_at", createdAt.toString());

        return result;
    }
}

