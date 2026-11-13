
package org.demo.example.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import org.demo.example.model.MeldingOverzicht;
import org.demo.example.repository.MeldingRepository;

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

        loadMeldingen();
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
            meldingenTable.setItems(
                    FXCollections.observableArrayList(
                            repository.findAllOverzicht()
                    )
            );

        } catch (SQLException e) {

            e.printStackTrace();

            Alert alert = new Alert(
                    Alert.AlertType.ERROR
            );

            alert.setTitle("Databasefout");
            alert.setHeaderText(
                    "Meldingen laden mislukt"
            );
            alert.setContentText(e.getMessage());

            alert.showAndWait();
        }
    }
}
