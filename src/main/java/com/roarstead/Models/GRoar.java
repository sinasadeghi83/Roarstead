package com.roarstead.Models;

import com.roarstead.Components.Annotation.Exclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Set;

@Entity
@Table
public class GRoar extends Roar{

    public static final int MAX_CHARS_GROAR = 280;

    @ManyToOne
    @JoinColumn(name="writer_id", nullable = false)
    protected User writer;

    @Column
    @Size(max = MAX_CHARS_GROAR)
    protected String text;

    @Exclude
    @OneToMany(mappedBy = "groar")
    protected Set<RoarMedia> roarMediaSet;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", insertable = true, updatable = false)
    protected Date createdAt;

    @Exclude
    @ManyToMany
    @JoinTable(
            name = "likes",
            joinColumns = @JoinColumn(name = "roar_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    protected Set<User> usersLiked;

    @Exclude
    @OneToMany(mappedBy = "reroared")
    protected Set<Reroar> reroars;

    @Exclude
    @OneToMany(mappedBy = "quotedRoar")
    protected Set<QRoar> quotes;

    @PrePersist
    protected void onCreate() {
        super.onCreate();
        LocalDateTime currentDateTime = LocalDateTime.now();
        Date currentDate = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());
        this.createdAt = currentDate;
    }

    public GRoar(){}

    public GRoar(User writer, String text) {
        super(writer);
        this.writer = writer;
        this.text = text;
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
}
