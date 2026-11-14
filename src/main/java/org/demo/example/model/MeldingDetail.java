
package org.demo.example.model;

import java.time.LocalDateTime;
import java.util.List;

public record MeldingDetail(
        int meldingId,
        String fietsenstalling,
        String probleemtype,
        String status,
        Integer medewerkerId,
        String medewerker,
        LocalDateTime datum,
        String omschrijving,
        List<TaakDetail> taken
) {

    public record TaakDetail(
            int taakId,
            String omschrijving,
            boolean voltooid
    ) {}
}
