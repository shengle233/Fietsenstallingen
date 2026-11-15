
package org.demo.example.model;

import java.util.List;

public class Andersprobleem extends Probleem {

    @Override
    public String getType() {
        return "Anders";
    }

    @Override
    public List<String> maakTaken() {
        return List.of(
                "Onderzoek het gemelde probleem"
        );
    }
}
