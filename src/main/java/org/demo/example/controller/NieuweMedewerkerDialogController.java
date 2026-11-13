package org.demo.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.demo.example.model.Medewerker;

public class NieuweMedewerkerDialogController {

    @FXML
    private TextField naamField;

    @FXML
    private TextField emailField;

    @FXML
    private Label errorLabel;

    @FXML
    private Label titelLabel;

    public void setTitel(String titel) {
        titelLabel.setText(titel);
    }

    public String getNaam() {
        return naamField.getText().trim();
    }

    public String getEmail() {
        return emailField.getText().trim();
    }

    public void showError(String message) {
        errorLabel.setText(message);
    }

    public void setMedewerker(Medewerker medewerker) {
        naamField.setText(medewerker.getNaam());
        emailField.setText(medewerker.getEmail());
    }

}
