
package org.demo.example.model;

import java.util.List;

public class Verlichtingsprobleem extends Probleem {

    @Override
    public String getType() {
        return "Verlichting";
    }

    @Override
    public List<String> maakTaken() {
        return List.of(
                "Controleer de melding en de exacte locatie",
                "Inspecteer de defecte verlichting",
                "Beoordeel of de locatie veilig toegankelijk is",
                "Schakel indien nodig de stroom veilig uit",
                "Laat de defecte lamp of armatuur repareren of vervangen",
                "Test de verlichting na de reparatie",
                "Controleer of de omgeving weer veilig is",
                "Registreer de uitgevoerde werkzaamheden"
        );
    }
}
