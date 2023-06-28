package com.roarstead.Models;

import com.roarstead.App;
import com.roarstead.Components.Database.Database;
import com.roarstead.Components.Resource.Models.Image;
import com.roarstead.Components.Resource.ResourceManager;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.checkerframework.checker.units.qual.C;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
@Embeddable
public class Profile {

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "prof_img_id", referencedColumnName = "id")
    private Image profImage;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="header_img_id", referencedColumnName = "id")
    private Image headerImage;

    @Column(name = "bio")
    @Size(min = 0,max = 160,message = "bio must be between 0 and 160 characters")
    private String bio;

    @Column
    @NotNull(message = "location can not be NULL")
    private String location;

    @Column(name = "url")
    private URL webSiteLink;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", insertable = true, updatable = false)
    private Date createdAt;

    public Image getProfImage() {
        return profImage;
    }

    public void setProfImage(Image profImage) {
        this.profImage = profImage;
    }

    public Image getHeaderImage() {
        return headerImage;
    }

    public void setHeaderImage(Image headerImage) {
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

    public URL getWebSiteLink() {
        return webSiteLink;
    }

    public void setWebSiteLink(URL webSiteLink) {
        this.webSiteLink = webSiteLink;
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
}

