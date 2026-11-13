
package org.demo.example.model;

import java.time.LocalDateTime;

public class MeldingOverzicht {

    private final int meldingId;
    private final String fietsenstalling;
    private final String probleemtype;
    private final String status;
    private final String medewerker;
    private final LocalDateTime datum;

    public MeldingOverzicht(
            int meldingId,
            String fietsenstalling,
            String probleemtype,
            String status,
            String medewerker,
            LocalDateTime datum) {

        this.meldingId = meldingId;
        this.fietsenstalling = fietsenstalling;
        this.probleemtype = probleemtype;
        this.status = status;
        this.medewerker = medewerker;
        this.datum = datum;
    }

    public int getMeldingId() {
        return meldingId;
    }

    public String getFietsenstalling() {
        return fietsenstalling;
    }

    public String getProbleemtype() {
        return probleemtype;
    }

    public String getStatus() {
        return status;
    }

    public String getMedewerker() {
        return medewerker;
    }

    public LocalDateTime getDatum() {
        return datum;
    }
}
