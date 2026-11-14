
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
                "Fietsenstalling schoonmaken"
        );
    }
}
