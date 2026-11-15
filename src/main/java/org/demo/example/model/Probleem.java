package org.demo.example.model;

import java.util.List;

public abstract class Probleem {

    public abstract String getType();

    public abstract List<String> maakTaken();

    public static Probleem vanType(String type) {

        if (type == null) {
            throw new IllegalArgumentException(
                    "Selecteer een probleemtype."
            );
        }

        return switch (type) {
            case "Verlichting" -> new Verlichtingsprobleem();
            case "Schoonmaak" -> new Schoonmaakprobleem();
            case "Beschadiging" -> new Beschadigingsprobleem();
            case "Anders" -> new Andersprobleem();

            default -> throw new IllegalArgumentException(
                    "Onbekend probleemtype: " + type
            );
        };
    }
}