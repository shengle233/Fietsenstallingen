
package org.demo.example.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.stage.Stage;
import org.demo.example.model.MeldingOverzicht;
import org.demo.example.repository.MeldingRepository;

import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

public class MeldingController extends NavigationController {

    @FXML
    private TableView<MeldingOverzicht> meldingenTable;

    @FXML
    private TableColumn<MeldingOverzicht, Integer> idColumn;

    @FXML
    private TableColumn<MeldingOverzicht, String> fietsenstallingColumn;

    @FXML
    private TableColumn<MeldingOverzicht, String> probleemtypeColumn;

    @FXML
    private TableColumn<MeldingOverzicht, String> statusColumn;

    @FXML
    private TableColumn<MeldingOverzicht, String> medewerkerColumn;

    @FXML
    private TableColumn<MeldingOverzicht, String> datumColumn;

    @FXML
    private TextField zoekField;

    @FXML
    private ComboBox<String> statusComboBox;

    private FilteredList<MeldingOverzicht> filteredMeldingen;

    private final MeldingRepository repository =
            new MeldingRepository();

    private final DateTimeFormatter dateFormatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @FXML
    public void initialize() {

        // ID
        idColumn.setCellValueFactory(data ->
                new ReadOnlyObjectWrapper<>(
                        data.getValue().getMeldingId()
                )
        );

        // Fietsenstalling
        fietsenstallingColumn.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getFietsenstalling()
                )
        );

        // Probleemtype
        probleemtypeColumn.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getProbleemtype()
                )
        );

        // Status
        statusColumn.setCellValueFactory(data ->
                new SimpleStringProperty(
                        formatStatus(data.getValue().getStatus())
                )
        );

        // Medewerker
        medewerkerColumn.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getMedewerker()
                )
        );

        // Datum
        datumColumn.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getDatum()
                                .format(dateFormatter)
                )
        );

        statusComboBox.getItems().addAll(
                "Alle statussen",
                "Open",
                "In behandeling",
                "Onderbroken",
                "Afgehandeld"
        );

        statusComboBox.setValue("Alle statussen");

        setupFilters();

        loadMeldingen();

        setupDetailNavigation();
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




    private void loadMeldingen() {

        try {
            ObservableList<MeldingOverzicht> meldingen =
                    FXCollections.observableArrayList(
                            repository.findAllOverzicht()
                    );

            filteredMeldingen = new FilteredList<>(
                    meldingen,
                    melding -> true
            );

            SortedList<MeldingOverzicht> sortedMeldingen =
                    new SortedList<>(filteredMeldingen);

            sortedMeldingen.comparatorProperty().bind(
                    meldingenTable.comparatorProperty()
            );

            meldingenTable.setItems(sortedMeldingen);

            updateFilter();

        } catch (SQLException e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Databasefout");
            alert.setHeaderText("Meldingen laden mislukt");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


    private void setupFilters() {

        zoekField.textProperty().addListener(
                (obs, oldValue, newValue) -> updateFilter()
        );

        statusComboBox.valueProperty().addListener(
                (obs, oldValue, newValue) -> updateFilter()
        );
    }


    private void updateFilter() {

        if (filteredMeldingen == null) {
            return;
        }

        String zoekterm = zoekField.getText()
                .toLowerCase()
                .trim();

        String geselecteerdeStatus =
                statusComboBox.getValue();

        filteredMeldingen.setPredicate(melding -> {

            String meldingStatus =
                    formatStatus(melding.getStatus());

            boolean statusMatch =
                    geselecteerdeStatus == null
                            || geselecteerdeStatus.equals("Alle statussen")
                            || meldingStatus.equals(geselecteerdeStatus);

            boolean zoekMatch =
                    zoekterm.isEmpty()
                            || String.valueOf(melding.getMeldingId())
                            .contains(zoekterm)
                            || melding.getFietsenstalling()
                            .toLowerCase().contains(zoekterm)
                            || melding.getProbleemtype()
                            .toLowerCase().contains(zoekterm)
                            || melding.getMedewerker()
                            .toLowerCase().contains(zoekterm);

            return statusMatch && zoekMatch;
        });
    }


    private void setupDetailNavigation() {

        meldingenTable.setRowFactory(table -> {

            TableRow<MeldingOverzicht> row = new TableRow<>();

            row.setOnMouseClicked(event -> {

                if (event.getClickCount() == 2
                        && !row.isEmpty()) {

                    openMeldingDetail(
                            row.getItem().getMeldingId()
                    );
                }
            });

            return row;
        });
    }

    private void openMeldingDetail(int meldingId) {

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/org/demo/example/melding-detail-view.fxml"
                    )
            );

            Parent root = loader.load();

            MeldingDetailController controller =
                    loader.getController();

            controller.loadMelding(meldingId);

            Stage stage = (Stage)
                    meldingenTable.getScene().getWindow();

            stage.getScene().setRoot(root);

        } catch (IOException e) {

            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fout");
            alert.setHeaderText("Detailpagina openen mislukt");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }



}
