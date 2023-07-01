package com.roarstead.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

//quote_roar

@Entity
@Table(name = "quote_roar")
public class QRoar extends GRoar{

    @ManyToOne
    @JoinColumn(name = "quoted_roar_id", nullable = false)
    protected GRoar quotedRoar;

    public GRoar getQuotedRoar() {
        return quotedRoar;
    }

    public void setQuotedRoar(GRoar quotedRoar) {
        this.quotedRoar = quotedRoar;
    }
}
