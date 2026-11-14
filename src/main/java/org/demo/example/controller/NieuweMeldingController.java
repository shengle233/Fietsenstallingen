package org.demo.example.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.demo.example.model.Fietsenstalling;
import org.demo.example.model.Medewerker;
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

    @FXML private TextField eersteTaakField;

    @FXML private VBox takenContainer;

    @FXML private Button opslaanButton;

    private final MedewerkerRepository medewerkerRepository =
            new MedewerkerRepository();

    private final MeldingRepository meldingRepository =
            new MeldingRepository();

    @FXML
    public void initialize() {
        loadFietsenstallingen();

        loadMedewerkers();

        loadProbleemtypes();

        setupProbleemSuggesties();
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
                "Beschadiging"
        );
    }

    private List<String> verzamelTaken() {

        List<String> taken = new ArrayList<>();

        // De eerste taak
        if (eersteTaakField.getText() != null
                && !eersteTaakField.getText().isBlank()) {

            taken.add(eersteTaakField.getText().trim());
        }

        // Extra taken
        for (Node node : takenContainer.getChildren()) {

            if (node instanceof TextField taakField) {

                String tekst = taakField.getText();

                if (tekst != null && !tekst.isBlank()) {
                    taken.add(tekst.trim());
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

        String probleemtype = probleemtypeComboBox.getValue();

        if (probleemtype == null) {
            toonFout("Selecteer een probleemtype.");
            return;
        }

        Probleem probleem = Probleem.vanType(probleemtype);

        String omschrijving =
                omschrijvingArea.getText();

        List<String> taken = verzamelTaken();

        if (fietsenstalling == null) {
            toonFout("Selecteer een fietsenstalling.");
            return;
        }

        Integer medewerkerId = medewerker == null
                ? null
                : medewerker.getMedewerkerId();

        try {


            int nieuweId = meldingRepository.createMelding(
                    fietsenstalling.getFietsenstallingId(),
                    medewerkerId,
                    omschrijving,
                    probleem.getType() ,
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

            // Terug naar het overzicht
            PageNavigator.navigate(
                    opslaanButton,
                    "melding-view.fxml"
            );

        } catch (IllegalArgumentException e) {
            toonFout(e.getMessage());

        } catch (SQLException e) {
            e.printStackTrace();
            toonFout("Databasefout: " + e.getMessage());
        }
    }

    private void toonFout(String bericht) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fout");
        alert.setHeaderText("Melding opslaan mislukt");
        alert.setContentText(bericht);
        alert.showAndWait();
    }


    private String laatsteSuggestie;

    private void setupProbleemSuggesties() {

        probleemtypeComboBox.valueProperty().addListener(
                (observable, oudType, nieuwType) -> {

                    if (nieuwType == null) {
                        return;
                    }

                    Probleem probleem = Probleem.vanType(nieuwType);

                    String huidigeTekst = eersteTaakField.getText();

                    if (huidigeTekst == null
                            || huidigeTekst.isBlank()
                            || huidigeTekst.equals(laatsteSuggestie)) {

                        String suggestie =
                                probleem.maakTaken().get(0);

                        eersteTaakField.setText(suggestie);
                        laatsteSuggestie = suggestie;
                    }
                }
        );
    }




}
