package com.roarstead.Models;

import com.roarstead.Components.Resource.Models.Image;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.net.URL;
import java.util.Date;
@Embeddable
public class Profile {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "prof_img_id", referencedColumnName = "id")
    private Image profImage;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="header_img_id", referencedColumnName = "id")
    private Image headerImage;

    @Size(min = 0,max = 160,message = "bio must be between 0 and 160 characters")
    private String bio;

    @NotNull(message = "location can not be NULL")
    private String location;

    @Column(name = "url")
    private URL webSiteLink;

    @NotNull(message = "joinDate con not be NUll")
    private Date joinDate;

    public Object getProfImage() {
        return profImage;
    }

    public void setProfImage(Image profImage) {
        this.profImage = profImage;
    }

    public Object getHeaderImage() {
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

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }
}

