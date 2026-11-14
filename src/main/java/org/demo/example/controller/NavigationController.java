
package org.demo.example.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import org.demo.example.util.PageNavigator;

public class NavigationController {

    @FXML
    protected void showDashboard(ActionEvent event) {
        PageNavigator.navigate(
                (Node) event.getSource(),
                "home-view.fxml"
        );
    }

    @FXML
    protected void showMedewerkers(ActionEvent event) {
        PageNavigator.navigate(
                (Node) event.getSource(),
                "medewerker-view.fxml"
        );
    }

    @FXML
    protected void showNieuweMelding(ActionEvent event) {
        PageNavigator.navigate(
                (Node) event.getSource(),
                "nieuwe-melding-view.fxml"
        );
    }

    @FXML
    protected void showMeldingen(ActionEvent event) {
        PageNavigator.navigate(
                (Node) event.getSource(),
                "melding-view.fxml"
        );
    }


    @FXML
    protected void afsluiten(ActionEvent event) {

        Alert bevestiging = new Alert(
                Alert.AlertType.CONFIRMATION
        );

        bevestiging.setTitle("Applicatie afsluiten");
        bevestiging.setHeaderText(
                "Weet je zeker dat je wilt afsluiten?"
        );
        bevestiging.setContentText(
                "De applicatie wordt afgesloten."
        );

        bevestiging.showAndWait().ifPresent(result -> {

            if (result == ButtonType.OK) {
                Platform.exit();
            }
        });
    }

}
