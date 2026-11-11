package org.example.demo.model;

public class Fietsenstalling {
    private int fietsenstallingId;
    private String naam;
    private String adres;

    public Fietsenstalling(int fietsenstallingId, String naam, String adres) {
        this.fietsenstallingId = fietsenstallingId;
        this.naam = naam;
        this.adres = adres;
    }

    public int getFietsenstallingId() {
        return fietsenstallingId;
    }

    public String getNaam() {
        return naam;
    }

    public String getAdres() {
        return adres;
    }
}

