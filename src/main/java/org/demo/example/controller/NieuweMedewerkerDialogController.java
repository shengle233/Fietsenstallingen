package org.demo.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class NieuweMedewerkerDialogController {

    @FXML
    private TextField naamField;

    @FXML
    private TextField emailField;

    @FXML
    private Label errorLabel;

    public String getNaam() {
        return naamField.getText().trim();
    }

    public String getEmail() {
        return emailField.getText().trim();
    }

    public void showError(String message) {
        errorLabel.setText(message);
    }
}
