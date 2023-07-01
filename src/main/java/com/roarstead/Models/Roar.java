package com.roarstead.Models;

import com.google.gson.annotations.SerializedName;
import com.roarstead.App;
import com.roarstead.Components.Database.Database;
import jakarta.persistence.*;

import javax.xml.crypto.Data;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Roar {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    protected int id;

    @ManyToOne
    @JoinColumn(name="sender_id", nullable = false)
    protected User sender;

    @SerializedName("sent_at")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "sent_at", insertable = true, updatable = false)
    protected Date sentAt;

    @ManyToMany
    @JoinTable(
            name = "roar_hashtag",
            joinColumns = @JoinColumn(name="roar_id"),
            inverseJoinColumns =  @JoinColumn(name="hashtag_id")
    )
    Set<Hashtag> hashtags;

    public Roar(){}

    public Roar(User sender) {
        this.sender = sender;
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        Date currentDate = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());
        this.sentAt = currentDate;
    }

    protected void addHashtags(Set<Hashtag> hashtags){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public Set<Hashtag> getHashtags() {
        return hashtags;
    }

    public void setHashtags(Set<Hashtag> hashtags) {
        this.hashtags = hashtags;
    }
}
