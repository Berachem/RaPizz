package com.example.rapizzapp.controllers;

import com.example.rapizzapp.RaPizzApplication;
import com.example.rapizzapp.entities.Client;
import com.example.rapizzapp.handlers.UserHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;

public class AdminDashboardController{
    public void logout(ActionEvent actionEvent) {
        System.out.println("Disconnected " + UserHandler.getInstance().getClient().getRole().toLowerCase() + " : "+UserHandler.getInstance().getClient().getNom()+" "+UserHandler.getInstance().getClient().getPrenom() + " | "+UserHandler.getInstance().getClient().getNumeroAbonnement());
        UserHandler.resetLogin();
        backToLogin(actionEvent);
    }

    @FXML
    public void backToLogin(ActionEvent event)  {
        Parent root;
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();

        try {
            // Hide this current window
            node.getScene().getWindow().hide();
            Scene scene = null;
            root = FXMLLoader.load(RaPizzApplication.class.getResource("login.fxml"));

            scene = new Scene(root, 320, 440);

            scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
