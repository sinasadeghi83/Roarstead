package com.roarstead.Models;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.net.URL;
import java.util.Date;
@Embeddable
public class Profile {
    private Object profImage;

    private Object headerImage;

    @Size(min = 0,max = 160,message = "bio must be between 0 and 160 characters")
    private String bio;

    @NotNull(message = "location can not be NULL")
    private String location;

    private URL webSiteLink;

    @NotNull(message = "joinDate con not be NUll")
    private Date joinDate;

    public Object getProfImage() {
        return profImage;
    }

    public void setProfImage(Object profImage) {
        this.profImage = profImage;
    }

    public Object getHeaderImage() {
        return headerImage;
    }

    public void setHeaderImage(Object headerImage) {
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

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }
}

