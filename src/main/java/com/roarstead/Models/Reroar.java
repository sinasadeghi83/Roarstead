package com.roarstead.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Table
@Entity
public class Reroar extends Roar {

    public Reroar(User sender, GRoar reroared) {
        super(sender);
        this.reroared = reroared;
    }

    @ManyToOne
    @JoinColumn(name = "reroared_id", nullable = false)
    protected GRoar reroared;

    public GRoar getReroared() {
        return reroared;
    }

    public void setReroared(GRoar reroared) {
        this.reroared = reroared;
    }
}
