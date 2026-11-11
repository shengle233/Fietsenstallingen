package org.example.demo.model;

import java.util.List;

public class Schoonmaakprobleem extends Probleem {

    @Override
    public List<String> maakTaken() {
        return List.of(
                "Controleer de vervuiling",
                "Verzamel schoonmaakmiddelen",
                "Maak de locatie schoon"
        );
    }
}
