package com.roarstead.Models;

import com.google.gson.annotations.SerializedName;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

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
}
