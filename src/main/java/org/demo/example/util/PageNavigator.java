
package org.demo.example.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;

public class PageNavigator {

    public static void navigate(Node source, String fxmlFile) {

        try {
            URL resource = PageNavigator.class.getResource(
                    "/org/demo/example/" + fxmlFile
            );

            if (resource == null) {
                throw new IOException("FXML niet gevonden: " + fxmlFile);
            }

            FXMLLoader loader = new FXMLLoader(resource);
            Parent root = loader.load();

            Stage stage = (Stage) source.getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigatiefout");
            alert.setHeaderText("Pagina kan niet worden geopend");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
