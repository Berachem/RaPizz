package com.example.rapizzapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        // Si on est en avance : utiliser un Boostrap Custom
        // String css = this.getClass().getResource("/com/example/rapizzapp/styles/bootstrap.css").toExternalForm();
        // scene.getStylesheets().add(css);        scene.getStylesheets().add(css);
        stage.setTitle("Bienvenue sur RapizzApp ! 🍕");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}