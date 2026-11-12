package org.demo.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import org.demo.example.model.*;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/org/demo/example/home-view.fxml")
        );
        System.out.println(
                getClass().getResource("/org/demo/example/home-view.fxml")
        );

        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("Fietsenstallingen Beheersysteem");
        stage.setScene(scene);
        stage.show();


    }

    public static void main(String[] args) {
        launch();
    }
}

