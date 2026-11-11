package org.example.demo;

import javafx.application.Application;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import org.example.demo.model.*;


public class HelloApplication extends Application {

    public void start(Stage stage){
        Pane root = new Pane();
        Scene scene = new Scene(root, 1200,800);
        stage.setScene(scene);
        stage.show();

        Fietsenstalling heuvel = new Fietsenstalling(
                1,
                "Fietsenstalling Heuvel",
                "Ten Hagestraat 14, Eindhoven"
        );

        Medewerker medewerker = new Medewerker(
                1,
                "Jan",
                "jan@example.com"
        );

        Probleem probleem = new Verlichtingsprobleem();

        Melding melding = new Melding(
                1,
                "Verlichting defect",
                heuvel,
                probleem
        );

        melding.wijsMedewerkerToe(medewerker);

        int taakId = 1;

        for (String omschrijving : probleem.maakTaken()) {
            Taak taak = new Taak(
                    taakId++,
                    melding.getMeldingId(),
                    omschrijving
            );

            melding.voegTaakToe(taak);
        }

        System.out.println("Fietsenstalling: "
                + melding.getFietsenstalling().getNaam());

        System.out.println("Status: " + melding.getStatus());

        for (Taak taak : melding.getTaken()) {
            System.out.println("- " + taak.getOmschrijving());
        }
    }

    public static void main(String[] args){
        launch();
    }
}
