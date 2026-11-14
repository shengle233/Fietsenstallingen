package org.demo.example.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.demo.example.model.MeldingOverzicht;
import org.demo.example.repository.MeldingRepository;

import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class HomeController extends NavigationController{

    @FXML
    private Label openAantalLabel;

    @FXML
    private Label behandelingAantalLabel;

    @FXML
    private Label onderbrokenAantalLabel;

    private final MeldingRepository meldingRepository =
            new MeldingRepository();

    @FXML
    private TableView<MeldingOverzicht> recenteMeldingenTable;

    @FXML
    private TableColumn<MeldingOverzicht, Integer> recentIdColumn;

    @FXML
    private TableColumn<MeldingOverzicht, String> recentFietsenstallingColumn;

    @FXML
    private TableColumn<MeldingOverzicht, String> recentProbleemtypeColumn;

    @FXML
    private TableColumn<MeldingOverzicht, String> recentStatusColumn;

    @FXML
    private TableColumn<MeldingOverzicht, String> recentDatumColumn;




    @FXML
    public void initialize() {

        loadDashboardStatistieken();

        setupRecenteMeldingen();

        loadRecenteMeldingen();

        setupMeldingDetailNavigation();
    }

    private void loadDashboardStatistieken() {

        try {
            Map<String, Integer> aantallen =
                    meldingRepository.countByStatus();

            openAantalLabel.setText(
                    String.valueOf(aantallen.get("OPEN"))
            );

            behandelingAantalLabel.setText(
                    String.valueOf(aantallen.get("IN_BEHANDELING"))
            );

            onderbrokenAantalLabel.setText(
                    String.valueOf(aantallen.get("ONDERBROKEN"))
            );

        } catch (SQLException e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Databasefout");
            alert.setHeaderText(
                    "Dashboard statistieken laden mislukt"
            );
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


    private void setupRecenteMeldingen() {

        recentIdColumn.setCellValueFactory(data ->
                new ReadOnlyObjectWrapper<>(
                        data.getValue().getMeldingId()
                )
        );

        recentFietsenstallingColumn.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getFietsenstalling()
                )
        );

        recentProbleemtypeColumn.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getProbleemtype()
                )
        );

        recentStatusColumn.setCellValueFactory(data ->
                new SimpleStringProperty(
                        formatStatus(data.getValue().getStatus())
                )
        );

        recentDatumColumn.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getDatum().format(
                                DateTimeFormatter.ofPattern("dd-MM-yyyy")
                        )
                )
        );
    }

    private void loadRecenteMeldingen() {

        try {
            recenteMeldingenTable.setItems(
                    FXCollections.observableArrayList(
                            meldingRepository.findRecent(5)
                    )
            );

        } catch (SQLException e) {

            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Databasefout");
            alert.setHeaderText(
                    "Recente meldingen laden mislukt"
            );
            alert.setContentText(e.getMessage());
            alert.showAndWait();
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


    private void setupMeldingDetailNavigation() {

        recenteMeldingenTable.setRowFactory(table -> {

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
                    recenteMeldingenTable.getScene().getWindow();

            stage.getScene().setRoot(root);

        } catch (IOException e) {

            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fout");
            alert.setHeaderText("Melding openen mislukt");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }




}
