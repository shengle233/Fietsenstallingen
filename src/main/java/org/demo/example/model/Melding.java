package org.demo.example.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Melding {

    private int meldingId;
    private LocalDate datum;
    private String omschrijving;

    private MeldingStatus status;
    private Fietsenstalling fietsenstalling;
    private Medewerker medewerker;
    private Probleem probleem;

    private List<Taak> taken;

    public Melding(int meldingId,
                   String omschrijving,
                   Fietsenstalling fietsenstalling,
                   Probleem probleem) {

        if (fietsenstalling == null || probleem == null
                || omschrijving == null || omschrijving.isBlank()) {
            throw new IllegalArgumentException("Ongeldige melding");
        }

        this.meldingId = meldingId;
        this.omschrijving = omschrijving;
        this.fietsenstalling = fietsenstalling;
        this.probleem = probleem;

        this.datum = LocalDate.now();
        this.status = MeldingStatus.OPEN;
        this.medewerker = null;
        this.taken = new ArrayList<>();
    }

    public int getMeldingId() {
        return meldingId;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public String getOmschrijving() {
        return omschrijving;
    }

    public MeldingStatus getStatus() {
        return status;
    }

    public Fietsenstalling getFietsenstalling() {
        return fietsenstalling;
    }

    public Medewerker getMedewerker() {
        return medewerker;
    }

    public Probleem getProbleem() {
        return probleem;
    }

    public List<Taak> getTaken() {
        return List.copyOf(taken);
    }

    public void voegTaakToe(Taak taak) {
        if (taak == null || taak.getMeldingId() != meldingId) {
            throw new IllegalArgumentException("Ongeldige taak");
        }

        taken.add(taak);
    }

    public void wijsMedewerkerToe(Medewerker medewerker) {
        if (medewerker == null) {
            throw new IllegalArgumentException("Medewerker is verplicht");
        }

        this.medewerker = medewerker;
    }
}
