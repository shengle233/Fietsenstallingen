
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
                "Verlichting controleren"
        );
    }
}
