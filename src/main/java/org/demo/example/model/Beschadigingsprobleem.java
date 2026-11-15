
package org.demo.example.model;

import java.util.List;

public class Beschadigingsprobleem extends Probleem {

    @Override
    public String getType() {
        return "Beschadiging";
    }

    @Override
    public List<String> maakTaken() {
        return List.of(
                "Controleer de melding en de exacte locatie",
                "Inspecteer de schade aan de voorziening",
                "Beoordeel de ernst en mogelijke veiligheidsrisico's",
                "Scherm gevaarlijke onderdelen indien nodig af",
                "Bepaal welke reparatie of vervanging nodig is",
                "Laat de beschadigde onderdelen herstellen of vervangen",
                "Controleer de werking en veiligheid na herstel",
                "Registreer de uitgevoerde reparatiewerkzaamheden"
        );
    }
}
