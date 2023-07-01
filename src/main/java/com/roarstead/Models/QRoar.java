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

    public QRoar(){}

    public QRoar(GRoar quotedRoar) {
        this.quotedRoar = quotedRoar;
    }

    public QRoar(User writer, String text, GRoar quotedRoar) {
        super(writer, text);
        this.quotedRoar = quotedRoar;
    }

    public QRoar(User writer, String text, GRoar replyTo, GRoar quotedRoar) {
        super(writer, text, replyTo);
        this.quotedRoar = quotedRoar;
    }

    public GRoar getQuotedRoar() {
        return quotedRoar;
    }

    public void setQuotedRoar(GRoar quotedRoar) {
        this.quotedRoar = quotedRoar;
    }
}
