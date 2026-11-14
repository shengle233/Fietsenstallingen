
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
                "Beschadiging inspecteren"
        );
    }
}
