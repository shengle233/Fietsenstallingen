package org.demo.example.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.demo.example.model.Fietsenstalling;
import org.demo.example.model.Medewerker;
import org.demo.example.model.Melding;
import org.demo.example.repository.FietsenstallingRepository;
import org.demo.example.repository.MedewerkerRepository;
import org.demo.example.repository.MeldingRepository;
import org.demo.example.util.PageNavigator;
import org.demo.example.model.Probleem;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class NieuweMeldingController extends NavigationController{


    @FXML
    private ComboBox<Fietsenstalling> fietsenstallingComboBox;

    private final FietsenstallingRepository
            fietsenstallingRepository =
            new FietsenstallingRepository();

    @FXML
    private ComboBox<Medewerker> medewerkerComboBox;

    @FXML
    private ComboBox<String> probleemtypeComboBox;

    @FXML private TextArea omschrijvingArea;

    @FXML private VBox takenContainer;

    @FXML private Button opslaanButton;

    private final MedewerkerRepository medewerkerRepository =
            new MedewerkerRepository();

    private final MeldingRepository meldingRepository =
            new MeldingRepository();

    private List<String> laatsteSuggesties = List.of();

    @FXML
    public void initialize() {
        loadFietsenstallingen();

        loadMedewerkers();

        loadProbleemtypes();

        setupProbleemSuggesties();

        voegTaakVeldToe("");
    }

    private void loadFietsenstallingen() {

        try {
            fietsenstallingComboBox.setItems(
                    FXCollections.observableArrayList(
                            fietsenstallingRepository.findAll()
                    )
            );

        } catch (SQLException e) {

            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Databasefout");
            alert.setHeaderText(
                    "Fietsenstallingen laden mislukt"
            );
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


    private void loadMedewerkers() {

        try {
            medewerkerComboBox.setItems(
                    FXCollections.observableArrayList(
                            medewerkerRepository.findAll()
                    )
            );

        } catch (SQLException e) {

            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Databasefout");
            alert.setHeaderText(
                    "Medewerkers laden mislukt"
            );
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void loadProbleemtypes() {

        probleemtypeComboBox.getItems().setAll(
                "Verlichting",
                "Schoonmaak",
                "Beschadiging",
                "Anders"
        );
    }


    private List<String> verzamelTaken() {

        List<String> taken = new ArrayList<>();

        for (Node node : takenContainer.getChildren()) {

            if (node instanceof VBox kaart) {

                for (Node onderdeel : kaart.getChildren()) {

                    if (onderdeel instanceof TextField taakField) {

                        String tekst = taakField.getText();

                        if (tekst != null && !tekst.isBlank()) {
                            taken.add(tekst.trim());
                        }
                    }
                }
            }
        }

        return taken;
    }




    @FXML
    private void opslaanMelding() {

        Fietsenstalling fietsenstalling =
                fietsenstallingComboBox.getValue();

        Medewerker medewerker =
                medewerkerComboBox.getValue();

        String probleemtype =
                probleemtypeComboBox.getValue();

        String omschrijving =
                omschrijvingArea.getText();

        List<String> taken = verzamelTaken();

        if (fietsenstalling == null) {
            toonFout("Selecteer een fietsenstalling.");
            return;
        }

        if (probleemtype == null) {
            toonFout("Selecteer een probleemtype.");
            return;
        }

        if (taken.isEmpty()) {
            toonFout("Voeg minimaal één taak toe.");
            return;
        }

        try {
            Probleem probleem =
                    Probleem.vanType(probleemtype);

            Melding melding = new Melding(
                    omschrijving,
                    fietsenstalling,
                    probleem
            );

            if (medewerker != null) {
                melding.wijsMedewerkerToe(medewerker);
            }

            int nieuweId = meldingRepository.createMelding(
                    melding,
                    taken
            );

            Alert alert = new Alert(
                    Alert.AlertType.INFORMATION
            );

            alert.setTitle("Gelukt");
            alert.setHeaderText("Melding aangemaakt");
            alert.setContentText(
                    "Melding #" + nieuweId + " is opgeslagen."
            );

            alert.showAndWait();

            PageNavigator.navigate(
                    opslaanButton,
                    "melding-view.fxml"
            );

        } catch (IllegalArgumentException e) {
            toonFout(e.getMessage());

        } catch (SQLException e) {
            e.printStackTrace();
            toonFout(
                    "Databasefout: " + e.getMessage()
            );
        }
    }


    private void toonFout(String bericht) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fout");
        alert.setHeaderText("Melding opslaan mislukt");
        alert.setContentText(bericht);
        alert.showAndWait();
    }






    private boolean probleemtypeWijzigingBezig = false;

    private void setupProbleemSuggesties() {

        probleemtypeComboBox.valueProperty().addListener(
                (observable, oudType, nieuwType) -> {

                    if (probleemtypeWijzigingBezig || nieuwType == null) {
                        return;
                    }

                    List<String> huidigeTaken = verzamelTaken();

                    boolean takenGewijzigd =
                            !huidigeTaken.isEmpty()
                                    && !huidigeTaken.equals(laatsteSuggesties);

                    if (takenGewijzigd) {

                        ButtonType vervangen = new ButtonType(
                                "Ja, vervangen",
                                ButtonBar.ButtonData.OK_DONE
                        );

                        ButtonType annuleren = new ButtonType(
                                "Annuleren",
                                ButtonBar.ButtonData.CANCEL_CLOSE
                        );

                        Alert bevestiging = new Alert(
                                Alert.AlertType.CONFIRMATION
                        );

                        bevestiging.setTitle("Probleemtype wijzigen");
                        bevestiging.setHeaderText(
                                "Je hebt de taken aangepast."
                        );
                        bevestiging.setContentText(
                                "Wil je de huidige taken vervangen "
                                        + "door de voorgestelde taken?"
                        );

                        bevestiging.getButtonTypes().setAll(
                                vervangen, annuleren
                        );

                        if (bevestiging.showAndWait()
                                .orElse(annuleren) != vervangen) {

                            probleemtypeWijzigingBezig = true;

                            try {
                                probleemtypeComboBox.setValue(oudType);
                            } finally {
                                probleemtypeWijzigingBezig = false;
                            }

                            return;
                        }
                    }

                    Probleem probleem = Probleem.vanType(nieuwType);

                    List<String> suggesties = probleem.maakTaken();

                    takenContainer.getChildren().clear();

                    for (String suggestie : suggesties) {
                        voegTaakVeldToe(suggestie);
                    }

                    laatsteSuggesties = List.copyOf(suggesties);
                }
        );
    }






    @FXML
    private void taakToevoegen() {

        voegTaakVeldToe("");
    }

    private void voegTaakVeldToe(String tekst) {

        VBox taakKaart = new VBox(8);
        taakKaart.getStyleClass().add("nieuwe-taak-card");

        Label taakLabel = new Label();
        taakLabel.setId("taakNummerLabel");
        taakLabel.getStyleClass().add("form-label");

        TextField taakField = new TextField();
        taakField.setText(tekst);
        taakField.setPromptText("Beschrijf de taak...");
        taakField.getStyleClass().add("form-input");
        taakField.setPrefHeight(42);

        Button verwijderButton = new Button("×");
        verwijderButton.getStyleClass().add("taak-delete-button");
        verwijderButton.setTooltip(
                new Tooltip("Taak verwijderen")
        );
        verwijderButton.setPrefSize(30, 30);

        verwijderButton.setOnAction(event -> {
            takenContainer.getChildren().remove(taakKaart);
            updateTaakNummers();
        });

        HBox header = new HBox(12, taakLabel, verwijderButton);
        header.setAlignment(Pos.CENTER_LEFT);

        HBox.setHgrow(
                taakLabel,
                Priority.ALWAYS
        );

        taakKaart.getChildren().addAll(header, taakField);

        takenContainer.getChildren().add(taakKaart);

        updateTaakNummers();
    }


    private void updateTaakNummers() {

        int nummer = 1;

        for (Node node : takenContainer.getChildren()) {

            if (node instanceof VBox kaart) {

                Label label = (Label) kaart.lookup(
                        "#taakNummerLabel"
                );

                if (label != null) {
                    label.setText("Taak " + nummer);
                    nummer++;
                }
            }
        }
    }






}
