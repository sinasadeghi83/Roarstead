package com.roarstead.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "reRoar")
public class ReRoar {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false,unique = true)
    private String username;

    @Column()
    private String content;
    @ManyToOne
    @JoinColumn(name = "roar-id")
    private Roar roar;

    public ReRoar() {

    }
    public ReRoar(String username, String content, Roar roar) {
        this.username = username;
        this.content = content;
        this.roar = roar;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Roar getRoar() {
        return roar;
    }

    public void setRoar(Roar roar) {
        this.roar = roar;
    }
}
