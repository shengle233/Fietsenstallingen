package org.example.demo.model;

public class Medewerker {
    private int medewerkerId;
    private String naam;
    private String email;

    public Medewerker(int medewerkerId, String naam, String email) {
        this.medewerkerId = medewerkerId;
        this.naam = naam;
        this.email = email;
    }

    public int getMedewerkerId() {
        return medewerkerId;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

