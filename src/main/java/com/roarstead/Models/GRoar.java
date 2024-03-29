package com.roarstead.Models;

import com.google.gson.annotations.SerializedName;
import com.roarstead.App;
import com.roarstead.Components.Annotation.Exclude;
import com.roarstead.Components.Database.Database;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table
public class GRoar extends Roar{

    public static final int MAX_CHARS_GROAR = 280;
    public static final int MIN_CHARS_GROAR = 4;

    @ManyToOne
    @JoinColumn(name="writer_id", nullable = false)
    protected User writer;

    @Column
    @Size(max = MAX_CHARS_GROAR)
    protected String text;

    @Exclude
    @OneToMany(mappedBy = "groar")
    protected Set<RoarMedia> roarMediaSet;

    @SerializedName("created_at")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", insertable = true, updatable = false)
    protected Date createdAt;

    @Exclude
    @OneToMany(mappedBy = "reroared")
    protected Set<Reroar> reroars;

    @Exclude
    @OneToMany(mappedBy = "quotedRoar")
    protected Set<QRoar> quotes;

    @Exclude
    @OneToMany(mappedBy = "replyTo")
    protected Set<GRoar> replies;

    @SerializedName("reply_to")
    @ManyToOne
    @JoinColumn(name = "reply_to")
    protected GRoar replyTo;

    @Exclude
    @ManyToMany
    @JoinTable(
            name = "groar_like",
            joinColumns = @JoinColumn(name="groar_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "user_liked_id", nullable = false)
    )
    protected Set<User> usersLiked;

    @PrePersist
    protected void onCreate() {
        super.onCreate();
        LocalDateTime currentDateTime = LocalDateTime.now();
        this.createdAt = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());
        hashtags = Hashtag.extractHashtags(this.text);
    }

    public GRoar(){}

    public GRoar(User writer, String text) {
        super(writer);
        this.writer = writer;
        this.text = text;
    }

    public GRoar(User writer, String text, GRoar replyTo) {
        super(writer);
        this.writer = writer;
        this.text = text;
        this.replyTo = replyTo;
    }

    public void like(User user) {
        if(usersLiked == null)
            usersLiked = new HashSet<>();
        usersLiked.add(user);
    }

    public void unLike(User user) {
        if(usersLiked == null)
            return;
        usersLiked.remove(user);
    }

    public User getWriter() {
        return writer;
    }

    public void setWriter(User writer) {
        this.writer = writer;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Set<RoarMedia> getMediaSet() {
        return roarMediaSet;
    }

    public void setMediaSet(Set<RoarMedia> roarMediaSet) {
        this.roarMediaSet = roarMediaSet;
    }

    public Set<User> getUsersLiked() {
        return usersLiked;
    }

    public void setUsersLiked(Set<User> usersLiked) {
        this.usersLiked = usersLiked;
    }

    public Set<Reroar> getReroars() {
        return reroars;
    }

    public void setReroars(Set<Reroar> reroars) {
        this.reroars = reroars;
    }

    public Set<QRoar> getQuotes() {
        return quotes;
    }

    public void setQuotes(Set<QRoar> quotes) {
        this.quotes = quotes;
    }

    public GRoar getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(GRoar replyTo) {
        this.replyTo = replyTo;
    }

    public Set<GRoar> getReplies() {
        return replies;
    }

    public void setReplies(Set<GRoar> replies) {
        this.replies = replies;
    }
}
