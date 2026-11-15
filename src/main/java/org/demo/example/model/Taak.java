package org.demo.example.model;

public class Taak {
    private int taakId;
    private int meldingId;
    private String omschrijving;
    private boolean voltooid;

    public Taak(int taakId, int meldingId, String omschrijving) {
        this.taakId = taakId;
        this.meldingId = meldingId;
        this.omschrijving = omschrijving;
        this.voltooid = false;
    }


    public int getTaakId() {
        return taakId;
    }

    public int getMeldingId() {
        return meldingId;
    }

    public String getOmschrijving() {
        return omschrijving;
    }

    public void setOmschrijving(String omschrijving) {
        this.omschrijving = omschrijving;
    }

    public boolean isVoltooid() {
        return voltooid;
    }

    public void setVoltooid(boolean voltooid) {
        this.voltooid = voltooid;
    }
}
