package com.roarstead.Models;

import jakarta.persistence.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "roars")
public class Roar {
    @Id()
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "roar-id")
    private int id;
    @Column(name = "parent-id")
    private int parentId;
    @Column(name = "username",nullable = false, unique = true)
    private String username;
    @Column(nullable = false, length = 280)
    private String content;
    @Lob
    @Column
    private Byte[] image;

    @ManyToMany(mappedBy = "likedRoars")
    private Set<User> like = new HashSet<>();
    @ManyToOne
    @JoinColumn(name = "parent-id")
    private Roar comment;

    @OneToMany(mappedBy = "roar")
    private List<ReRoar> reRoars;

    @Column(name = "create-at")
    private LocalDate createdAt;

    public Roar() {

    }

    public Roar(String username,String content) {
        this.username = username;
        this.content = content;
        createdAt = LocalDate.now();
    }

    public void like(User user) {
        like.add(user);
    }
    public void unLike(User user) {
        like.remove(user);
    }

    public void reRoar(ReRoar reRoar) {
        this.reRoars.add(reRoar);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Byte[] getImage() {
        return image;
    }

    public void setImage(Byte[] image) {
        this.image = image;
    }

    public Set<User> getLike() {
        return like;
    }

    public void setLike(Set<User> like) {
        this.like = like;
    }

    public Roar getComment() {
        return comment;
    }

    public void setComment(Roar comment) {
        this.comment = comment;
    }

    public List<ReRoar> getReRoars() {
        return reRoars;
    }

    public void setReRoars(List<ReRoar> reRoars) {
        this.reRoars = reRoars;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public String getTimeElapsed() {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(createdAt, now);

        long seconds = duration.getSeconds();
        long minutes = seconds / 60;
        long hours = minutes / 60;
        int dayOfMount = createdAt.getDayOfMonth();
        String mount = createdAt.getMonth().toString();

        if (hours < 24) {
            if (hours > 0) {
                return hours + " hour(s) ago";
            } else if (minutes > 0) {
                return minutes + " minute(s) ago";
            } else {
                return seconds + " second(s) ago";
            }
        }
        return dayOfMount + mount;
    }
}
