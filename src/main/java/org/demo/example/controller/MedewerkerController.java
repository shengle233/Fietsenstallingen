
package org.demo.example.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import org.demo.example.model.Medewerker;
import org.demo.example.repository.MedewerkerRepository;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javafx.scene.control.TextField;


public class MedewerkerController extends NavigationController {

    @FXML
    private TableView<Medewerker> medewerkersTable;

    @FXML
    private TableColumn<Medewerker, Integer> idColumn;

    @FXML
    private TableColumn<Medewerker, String> naamColumn;

    @FXML
    private TableColumn<Medewerker, String> emailColumn;

    @FXML
    private TextField zoekField;

    @FXML
    private TableColumn<Medewerker, Integer> actieveMeldingenColumn;

    private FilteredList<Medewerker> filteredMedewerkers;

    private Map<Integer, Integer> actieveAantallen =
            new HashMap<>();


    private final MedewerkerRepository repository =
            new MedewerkerRepository();

    @FXML
    public void initialize() {

        idColumn.setCellValueFactory(data ->
                new ReadOnlyObjectWrapper<>(
                        data.getValue().getMedewerkerId()
                )
        );

        naamColumn.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getNaam()
                )
        );

        emailColumn.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getEmail()
                )
        );

        actieveMeldingenColumn.setCellValueFactory(data ->
                new ReadOnlyObjectWrapper<>(
                        actieveAantallen.getOrDefault(
                                data.getValue().getMedewerkerId(),
                                0
                        )
                )
        );

        zoekField.textProperty().addListener(
                (obs, oldValue, newValue) -> updateFilter()
        );


        loadMedewerkers();
    }


    private void loadMedewerkers() {

        try {
            ObservableList<Medewerker> medewerkers =
                    FXCollections.observableArrayList(
                            repository.findAll()
                    );

            actieveAantallen =
                    repository.countActieveMeldingen();

            filteredMedewerkers =
                    new FilteredList<>(medewerkers, m -> true);

            SortedList<Medewerker> sortedMedewerkers =
                    new SortedList<>(filteredMedewerkers);

            sortedMedewerkers.comparatorProperty().bind(
                    medewerkersTable.comparatorProperty()
            );

            medewerkersTable.setItems(sortedMedewerkers);

            updateFilter();

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


    @FXML
    private void openNieuweMedewerker() {

        // Buitenste try: FXML laden
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

            controller.setTitel("Nieuwe medewerker");

            ButtonType toevoegen = new ButtonType(
                    "Toevoegen",
                    ButtonBar.ButtonData.OK_DONE
            );

            ButtonType annuleren = new ButtonType(
                    "Annuleren",
                    ButtonBar.ButtonData.CANCEL_CLOSE
            );

            dialogPane.getButtonTypes().addAll(
                    annuleren,
                    toevoegen
            );

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Nieuwe medewerker");
            dialog.setDialogPane(dialogPane);
            dialog.setResizable(false);

            Button saveButton =
                    (Button) dialogPane.lookupButton(toevoegen);

            saveButton.setStyle(
                    "-fx-background-color: #263547;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 8px;"
            );

            // Controleer invoer voordat de dialog sluit
            saveButton.addEventFilter(
                    ActionEvent.ACTION,
                    event -> {

                        String naam = controller.getNaam();
                        String email = controller.getEmail();

                        // 1. Controleer verplichte velden
                        if (naam.isEmpty() || email.isEmpty()) {
                            controller.showError(
                                    "Vul alle verplichte velden in."
                            );
                            event.consume();
                            return;
                        }

                        // 2. Controleer e-mailadres
                        if (!email.matches(
                                "[^\\s@]+@[^\\s@]+\\.[^\\s@]+")) {

                            controller.showError(
                                    "Vul een geldig e-mailadres in."
                            );
                            event.consume();
                            return;
                        }

                        // 3. Sla de medewerker op in MySQL
                        try {
                            boolean success =
                                    repository.addMedewerker(naam, email);

                            if (success) {
                                loadMedewerkers();
                            } else {
                                controller.showError(
                                        "Medewerker kon niet worden opgeslagen."
                                );
                                event.consume();
                            }

                        } catch (SQLException e) {
                            e.printStackTrace();

                            controller.showError(
                                    "Databasefout: " + e.getMessage()
                            );

                            event.consume();
                        }
                    }
            );

            // Open de dialog
            dialog.showAndWait();

            // Opslaan gebeurt alleen bij een geldige klik
            // op Toevoegen (zie aangepaste code hieronder).

        } catch (IOException e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("FXML fout");
            alert.setHeaderText("Dialog kan niet worden geopend");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


    @FXML
    private void openBewerkMedewerker() {

        // 1. Haal de geselecteerde medewerker op
        Medewerker geselecteerdeMedewerker =
                medewerkersTable.getSelectionModel()
                        .getSelectedItem();

        if (geselecteerdeMedewerker == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Geen medewerker geselecteerd");
            alert.setHeaderText("Selecteer eerst een medewerker");
            alert.showAndWait();
            return;
        }

        try {
            // 2. Laad dezelfde dialog als bij toevoegen
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/org/demo/example/nieuwe-medewerker-dialog.fxml"
                    )
            );

            DialogPane dialogPane = new DialogPane();
            dialogPane.setContent(loader.load());

            NieuweMedewerkerDialogController controller =
                    loader.getController();

            controller.setTitel("Medewerker bewerken");

            // 3. Vul de bestaande gegevens in
            controller.setMedewerker(geselecteerdeMedewerker);

            ButtonType opslaan = new ButtonType(
                    "Opslaan",
                    ButtonBar.ButtonData.OK_DONE
            );

            ButtonType annuleren = new ButtonType(
                    "Annuleren",
                    ButtonBar.ButtonData.CANCEL_CLOSE
            );

            dialogPane.getButtonTypes().addAll(
                    annuleren, opslaan
            );

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Medewerker bewerken");
            dialog.setDialogPane(dialogPane);
            dialog.setResizable(false);

            Button saveButton =
                    (Button) dialogPane.lookupButton(opslaan);

            saveButton.setStyle(
                    "-fx-background-color: #263547;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 8px;"
            );

            // 4. Controleer invoer en sla op
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
                            return;
                        }

                        try {
                            boolean success =
                                    repository.updateMedewerker(
                                            geselecteerdeMedewerker
                                                    .getMedewerkerId(),
                                            naam,
                                            email
                                    );

                            if (success) {
                                loadMedewerkers();
                            } else {
                                controller.showError(
                                        "Medewerker niet gevonden."
                                );
                                event.consume();
                            }

                        } catch (SQLException e) {
                            e.printStackTrace();

                            controller.showError(
                                    "Databasefout: " + e.getMessage()
                            );
                            event.consume();
                        }
                    }
            );

            dialog.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("FXML fout");
            alert.setHeaderText(
                    "Bewerkvenster kan niet worden geopend"
            );
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


    @FXML
    private void deleteMedewerker() {

        Medewerker geselecteerdeMedewerker =
                medewerkersTable.getSelectionModel()
                        .getSelectedItem();

        // 1. Controleer selectie
        if (geselecteerdeMedewerker == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Geen medewerker geselecteerd");
            alert.setHeaderText("Selecteer eerst een medewerker");
            alert.showAndWait();
            return;
        }

        // 2. Vraag om bevestiging
        Alert bevestiging = new Alert(
                Alert.AlertType.CONFIRMATION
        );

        bevestiging.setTitle("Medewerker verwijderen");
        bevestiging.setHeaderText(
                "Weet je zeker dat je deze medewerker wilt verwijderen?"
        );
        bevestiging.setContentText(
                geselecteerdeMedewerker.getNaam()
        );

        Optional<ButtonType> result =
                bevestiging.showAndWait();

        if (result.isEmpty()
                || result.get() != ButtonType.OK) {
            return;
        }

        // 3. Verwijder uit database
        try {
            boolean success =
                    repository.deleteMedewerker(
                            geselecteerdeMedewerker.getMedewerkerId()
                    );

            if (success) {
                loadMedewerkers();
            } else {
                Alert alert = new Alert(
                        Alert.AlertType.WARNING
                );

                alert.setTitle("Verwijderen niet mogelijk");
                alert.setHeaderText(
                        "Medewerker kan niet worden verwijderd"
                );
                alert.setContentText(
                        "Deze medewerker heeft meldingen of bestaat niet meer."
                );
                alert.showAndWait();
            }

        } catch (SQLException e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Databasefout");
            alert.setHeaderText("Verwijderen mislukt");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


    private void updateFilter() {

        if (filteredMedewerkers == null) {
            return;
        }

        String zoekterm = zoekField.getText()
                .toLowerCase()
                .trim();

        filteredMedewerkers.setPredicate(medewerker -> {

            return zoekterm.isEmpty()
                    || String.valueOf(
                    medewerker.getMedewerkerId()
            ).contains(zoekterm)
                    || medewerker.getNaam()
                    .toLowerCase()
                    .contains(zoekterm)
                    || medewerker.getEmail()
                    .toLowerCase()
                    .contains(zoekterm);
        });
    }



}
