package org.demo.example.model;

import java.util.List;

public class Beschadigingsprobleem extends Probleem {

    @Override
    public List<String> maakTaken() {
        return List.of(
                "Controleer de beschadiging",
                "Bepaal welke onderdelen nodig zijn",
                "Repareer het beschadigde onderdeel",
                "Controleer de reparatie"
        );
    }
}
