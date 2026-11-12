
package org.demo.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
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
        Stage stage = (Stage)
                ((Node) event.getSource()).getScene().getWindow();

        stage.close();
    }
}
