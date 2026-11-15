
package org.demo.example.model;

import java.util.List;

public class Schoonmaakprobleem extends Probleem {

    @Override
    public String getType() {
        return "Schoonmaak";
    }

    @Override
    public List<String> maakTaken() {
        return List.of(
                "Controleer de melding en de exacte locatie",
                "Inspecteer de vervuilde locatie",
                "Beoordeel mogelijke veiligheids- of hygiënerisico's",
                "Zet de locatie indien nodig tijdelijk af",
                "Verwijder afval en andere vervuiling",
                "Reinig de vloer, rekken of andere getroffen onderdelen",
                "Controleer of de locatie schoon en veilig is",
                "Registreer de uitgevoerde schoonmaakwerkzaamheden"
        );
    }
}
