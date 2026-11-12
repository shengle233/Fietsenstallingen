package org.demo.example.model;

import java.util.List;

public class Verlichtingsprobleem extends Probleem {

    @Override
    public List<String> maakTaken() {
        return List.of(
                "Controleer de verlichting",
                "Verzamel de benodigde gereedschappen",
                "Repareer de verlichting",
                "Test de verlichting"
        );
    }
}

