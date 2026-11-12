package org.demo.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.*;
import java.io.IOException;
import java.util.Optional;

public class MedewerkerController extends NavigationController{

    @FXML
    private void openNieuweMedewerker() {

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/org/demo/example/nieuwe-medewerker-dialog.fxml"
                    )
            );

            DialogPane dialogPane = new DialogPane();
            dialogPane.setContent(loader.load());

            NieuweMedewerkerDialogController controller =
                    loader.getController();

            ButtonType toevoegen = new ButtonType(
                    "Toevoegen",
                    ButtonBar.ButtonData.OK_DONE
            );

            ButtonType annuleren = new ButtonType(
                    "Annuleren",
                    ButtonBar.ButtonData.CANCEL_CLOSE
            );

            dialogPane.getButtonTypes().addAll(
                    annuleren, toevoegen
            );

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Nieuwe medewerker");
            dialog.setDialogPane(dialogPane);
            dialog.setResizable(false);

            // De knoppen vormgeven
            Button saveButton =
                    (Button) dialogPane.lookupButton(toevoegen);

            saveButton.setStyle(
                    "-fx-background-color: #263547;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 8px;"
            );

            // Input controleren voordat dialoog sluit
            saveButton.addEventFilter(
                    ActionEvent.ACTION,
                    event -> {

                        String naam = controller.getNaam();
                        String email = controller.getEmail();

                        if (naam.isEmpty() || email.isEmpty()) {
                            controller.showError(
                                    "Vul alle verplichte velden in."
                            );
                            event.consume();
                            return;
                        }

                        if (!email.matches(
                                "[^\\s@]+@[^\\s@]+\\.[^\\s@]+")) {

                            controller.showError(
                                    "Vul een geldig e-mailadres in."
                            );
                            event.consume();
                        }
                    });

            Optional<ButtonType> result =
                    dialog.showAndWait();

            if (result.isPresent()
                    && result.get() == toevoegen) {

                String naam = controller.getNaam();
                String email = controller.getEmail();

                System.out.println(
                        "Nieuwe medewerker: " + naam
                );
                System.out.println(
                        "E-mail: " + email
                );

                // Hier later opslaan via MedewerkerRepository.
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
