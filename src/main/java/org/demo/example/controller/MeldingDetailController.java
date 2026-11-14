
package org.demo.example.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.demo.example.model.Medewerker;
import org.demo.example.model.MeldingDetail;
import org.demo.example.repository.MedewerkerRepository;
import org.demo.example.repository.MeldingRepository;

import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

public class MeldingDetailController {

    @FXML private Label meldingIdLabel;
    @FXML private Label fietsenstallingLabel;
    @FXML private Label probleemtypeLabel;
    @FXML private Label statusLabel;
    @FXML private Label datumLabel;

    @FXML private TextArea omschrijvingArea;
    @FXML private VBox takenContainer;

    @FXML private ComboBox<Medewerker> medewerkerComboBox;
    @FXML private Button wijzigMedewerkerButton;

    @FXML private Button onderbrekenButton;
    @FXML private Button hervattenButton;
    @FXML private Button afhandelenButton;

    @FXML private TextField nieuweTaakField;
    @FXML private Button taakToevoegenButton;
    @FXML private Button verwijderenButton;

    private final MeldingRepository repository =
            new MeldingRepository();

    private final MedewerkerRepository medewerkerRepository =
            new MedewerkerRepository();

    private final DateTimeFormatter dateFormatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    private int huidigeMeldingId;

    public void loadMelding(int meldingId) {

        try {
            MeldingDetail melding =
                    repository.findDetailById(meldingId);

            if (melding == null) {
                toonFout("Melding niet gevonden.");
                return;
            }

            huidigeMeldingId = melding.meldingId();

            meldingIdLabel.setText(
                    String.valueOf(melding.meldingId())
            );

            fietsenstallingLabel.setText(
                    melding.fietsenstalling()
            );

            probleemtypeLabel.setText(
                    melding.probleemtype()
            );

            statusLabel.setText(
                    formatStatus(melding.status())
            );

            datumLabel.setText(
                    melding.datum().format(dateFormatter)
            );

            omschrijvingArea.setText(
                    melding.omschrijving()
            );

            loadMedewerkers(melding.medewerkerId());

            boolean afgehandeld =
                    melding.status().equals("AFGEHANDELD");

            boolean onderbroken =
                    melding.status().equals("ONDERBROKEN");

            boolean taakToevoegenToegestaan =
                    !afgehandeld && !onderbroken;

            nieuweTaakField.setDisable(!taakToevoegenToegestaan);
            taakToevoegenButton.setDisable(!taakToevoegenToegestaan);

            boolean inBehandeling =
                    melding.status().equals("IN_BEHANDELING");

            boolean medewerkerAanwezig =
                    melding.medewerkerId() != null;

            boolean open =
                    "OPEN".equals(melding.status());

            verwijderenButton.setDisable(!open);

            medewerkerComboBox.setDisable(afgehandeld);
            wijzigMedewerkerButton.setDisable(afgehandeld);

            onderbrekenButton.setDisable(
                    !inBehandeling || !medewerkerAanwezig
            );

            hervattenButton.setDisable(
                    !onderbroken || !medewerkerAanwezig
            );

            boolean alleTakenVoltooid =
                    !melding.taken().isEmpty()
                            && melding.taken().stream()
                            .allMatch(MeldingDetail.TaakDetail::voltooid);

            afhandelenButton.setDisable(
                    !inBehandeling
                            || !medewerkerAanwezig
                            || !alleTakenVoltooid
            );


            takenContainer.getChildren().clear();

            var taken = melding.taken();

            for (int i = 0; i < taken.size(); i++) {

                MeldingDetail.TaakDetail taak = taken.get(i);

                CheckBox checkBox =
                        new CheckBox(taak.omschrijving());

                checkBox.getStyleClass().add("taak-checkbox");

                checkBox.setSelected(taak.voltooid());
                checkBox.setWrapText(true);
                checkBox.setMaxWidth(Double.MAX_VALUE);

                boolean takenGeblokkeerd =
                        afgehandeld
                                || onderbroken
                                || !medewerkerAanwezig;

                checkBox.setDisable(takenGeblokkeerd);

                checkBox.setOnAction(event -> {
                    wijzigTaak(
                            taak.taakId(),
                            checkBox.isSelected()
                    );
                });

                Button omhoogButton = new Button("↑");
                Button omlaagButton = new Button("↓");

                omhoogButton.getStyleClass().add("taak-move-button");
                omlaagButton.getStyleClass().add("taak-move-button");

                omhoogButton.setPrefSize(38, 34);
                omlaagButton.setPrefSize(38, 34);

                boolean verplaatsenGeblokkeerd =
                        afgehandeld || onderbroken;

                omhoogButton.setDisable(
                        verplaatsenGeblokkeerd || i == 0
                );

                omlaagButton.setDisable(
                        verplaatsenGeblokkeerd
                                || i == taken.size() - 1
                );

                omhoogButton.setOnAction(event ->
                        verplaatsTaak(taak.taakId(), -1)
                );

                omlaagButton.setOnAction(event ->
                        verplaatsTaak(taak.taakId(), 1)
                );

                HBox taakRow = new HBox(
                        12,
                        checkBox,
                        omhoogButton,
                        omlaagButton
                );

                taakRow.setAlignment(Pos.CENTER_LEFT);

                HBox.setHgrow(
                        checkBox,
                        javafx.scene.layout.Priority.ALWAYS
                );

                taakRow.getStyleClass().add("taak-row");

                takenContainer.getChildren().add(taakRow);
            }


        } catch (SQLException e) {
            e.printStackTrace();
            toonFout(e.getMessage());
        }
    }

    private void loadMedewerkers(Integer huidigeMedewerkerId) {

        try {
            var medewerkers = FXCollections.observableArrayList(
                    medewerkerRepository.findAll()
            );

            medewerkerComboBox.setItems(medewerkers);
            medewerkerComboBox.getSelectionModel().clearSelection();

            if (huidigeMedewerkerId == null) {
                return;
            }

            for (Medewerker medewerker : medewerkers) {

                if (medewerker.getMedewerkerId()
                        == huidigeMedewerkerId) {

                    medewerkerComboBox.setValue(medewerker);
                    break;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            toonFout(e.getMessage());
        }
    }

    @FXML
    private void wijzigMedewerker() {

        Medewerker medewerker =
                medewerkerComboBox.getValue();

        if (medewerker == null) {
            toonFout("Selecteer eerst een medewerker.");
            return;
        }

        try {
            boolean gelukt = repository.updateMedewerker(
                    huidigeMeldingId,
                    medewerker.getMedewerkerId()
            );

            if (!gelukt) {
                toonFout("Medewerker wijzigen is niet toegestaan.");
                loadMelding(huidigeMeldingId);
                return;
            }

            loadMelding(huidigeMeldingId);
            toonSucces("Medewerker gewijzigd.");

        } catch (SQLException e) {
            e.printStackTrace();
            toonFout(e.getMessage());
            loadMelding(huidigeMeldingId);
        }
    }

    private void wijzigTaak(int taakId, boolean voltooid) {

        try {
            boolean gelukt = repository.updateTaak(
                    huidigeMeldingId,
                    taakId,
                    voltooid
            );

            if (!gelukt) {
                toonFout("Deze taak kan niet worden gewijzigd.");
            }

            loadMelding(huidigeMeldingId);

        } catch (SQLException e) {
            e.printStackTrace();
            toonFout(e.getMessage());
            loadMelding(huidigeMeldingId);
        }
    }

    @FXML
    private void onderbrekenMelding() {
        wijzigStatus("ONDERBROKEN");
    }

    @FXML
    private void hervattenMelding() {
        wijzigStatus("IN_BEHANDELING");
    }

    @FXML
    private void afhandelenMelding() {

        Alert bevestiging = new Alert(
                Alert.AlertType.CONFIRMATION
        );

        bevestiging.setTitle("Melding afhandelen");
        bevestiging.setHeaderText(
                "Weet je zeker dat je deze melding wilt afhandelen?"
        );

        bevestiging.setContentText(
                "Na het afhandelen kan de melding niet meer "
                        + "worden gewijzigd."
        );

        if (bevestiging.showAndWait()
                .filter(button -> button == ButtonType.OK)
                .isEmpty()) {
            return;
        }

        wijzigStatus("AFGEHANDELD");
    }

    private void wijzigStatus(String nieuweStatus) {

        try {
            boolean gelukt = repository.updateStatus(
                    huidigeMeldingId,
                    nieuweStatus
            );

            if (!gelukt) {
                toonFout(
                        "Status wijzigen is niet toegestaan. "
                                + "Controleer de medewerker en taken."
                );

                loadMelding(huidigeMeldingId);
                return;
            }

            loadMelding(huidigeMeldingId);
            toonSucces("Status gewijzigd.");

        } catch (SQLException e) {
            e.printStackTrace();
            toonFout(e.getMessage());
            loadMelding(huidigeMeldingId);
        }
    }

    @FXML
    private void terugNaarMeldingen() {

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/org/demo/example/melding-view.fxml"
                    )
            );

            Parent root = loader.load();

            Stage stage = (Stage)
                    meldingIdLabel.getScene().getWindow();

            stage.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
            toonFout(e.getMessage());
        }
    }

    private String formatStatus(String status) {

        return switch (status) {
            case "OPEN" -> "Open";
            case "IN_BEHANDELING" -> "In behandeling";
            case "ONDERBROKEN" -> "Onderbroken";
            case "AFGEHANDELD" -> "Afgehandeld";
            default -> status;
        };
    }

    private void toonFout(String bericht) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fout");
        alert.setHeaderText("Actie mislukt");
        alert.setContentText(bericht);
        alert.showAndWait();
    }

    private void toonSucces(String bericht) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Gelukt");
        alert.setHeaderText(bericht);
        alert.showAndWait();
    }


    @FXML
    private void taakToevoegen() {

        String omschrijving = nieuweTaakField.getText();

        if (omschrijving == null || omschrijving.isBlank()) {
            toonFout("Vul een omschrijving voor de taak in.");
            return;
        }

        try {
            boolean gelukt = repository.addTaak(
                    huidigeMeldingId,
                    omschrijving
            );

            if (!gelukt) {
                toonFout(
                        "Taak toevoegen is niet toegestaan."
                );
                loadMelding(huidigeMeldingId);
                return;
            }

            nieuweTaakField.clear();

            loadMelding(huidigeMeldingId);

            toonSucces("Taak toegevoegd.");

        } catch (SQLException e) {
            e.printStackTrace();

            toonFout(e.getMessage());
            loadMelding(huidigeMeldingId);
        }
    }


    @FXML
    private void verwijderMelding() {

        Alert bevestiging = new Alert(
                Alert.AlertType.CONFIRMATION
        );

        bevestiging.setTitle("Melding verwijderen");
        bevestiging.setHeaderText(
                "Weet je zeker dat je deze melding wilt verwijderen?"
        );
        bevestiging.setContentText(
                "De melding en alle bijbehorende taken "
                        + "worden definitief verwijderd."
        );

        boolean bevestigd = bevestiging.showAndWait()
                .filter(button -> button == ButtonType.OK)
                .isPresent();

        if (!bevestigd) {
            return;
        }

        try {
            boolean gelukt =
                    repository.deleteMelding(huidigeMeldingId);

            if (!gelukt) {
                toonFout(
                        "Alleen meldingen met status OPEN "
                                + "kunnen worden verwijderd."
                );

                loadMelding(huidigeMeldingId);
                return;
            }

            Alert succes = new Alert(
                    Alert.AlertType.INFORMATION
            );

            succes.setTitle("Gelukt");
            succes.setHeaderText("Melding verwijderd");
            succes.setContentText(
                    "De melding en de bijbehorende taken "
                            + "zijn verwijderd."
            );
            succes.showAndWait();

            terugNaarMeldingen();

        } catch (SQLException e) {
            e.printStackTrace();
            toonFout(e.getMessage());
        }
    }


    private void verplaatsTaak(int taakId, int richting) {

        try {
            boolean gelukt = repository.moveTaak(
                    huidigeMeldingId,
                    taakId,
                    richting
            );

            if (!gelukt) {
                toonFout(
                        "De taak kan niet worden verplaatst."
                );
            }

            loadMelding(huidigeMeldingId);

        } catch (SQLException e) {
            e.printStackTrace();

            toonFout(e.getMessage());
            loadMelding(huidigeMeldingId);
        }
    }



}
