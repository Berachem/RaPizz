package com.example.rapizzapp.controllers;

import com.example.rapizzapp.RaPizzApplication;
import com.example.rapizzapp.entities.Client;
import com.example.rapizzapp.repositories.ClientRepository;
import com.example.rapizzapp.handlers.UserHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;

public class LoginController {
    @FXML
    public TextField abonnementField;
    private ClientRepository customerService;

    private UserHandler userHandler = UserHandler.getInstance();
    public void initialize() {
        customerService = ClientRepository.getInstance();
    }

    public boolean isInteger( String input ) {
        try {
            Integer.parseInt( input );
            return true;
        }
        catch( Exception e ) {
            return false;
        }
    }

    @FXML
    public void loginAction(ActionEvent event) {
        String numAbo = abonnementField.getText();
        if (numAbo.isEmpty()) {
            showAlert("Erreur", "Veuillez entrer votre numéro d'abonnement.", Alert.AlertType.ERROR);
            return;
        }

        if (isInteger(numAbo)){
            Client client = customerService.getClientByNumeroAbonnement(Integer.parseInt(numAbo));

            if (client != null) {
                userHandler.setClient(client);
                passLogin(event);
            } else {
                showAlert("Erreur", "Numéro d'abonnement incorrect.", Alert.AlertType.ERROR);
            }

        }else{
            showAlert("Erreur", "Veuillez entrer un numéro d'abonnement correct (uniquement des chiffres).", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void createAccountAction(ActionEvent event) {
        Parent root;
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();

        try {
            // Hide this current window
            node.getScene().getWindow().hide();

            //load next window
            root = FXMLLoader.load(RaPizzApplication.class.getResource("createAccount.fxml"));
            stage.setTitle("Créer un compte");
            Scene scene = new Scene(root, 340, 440);
            scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        // Ajoutez ici la logique pour créer un compte
    }

    @FXML
    public void passLogin(ActionEvent event){
        Parent root;
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();

        try {
            // Hide this current window
            node.getScene().getWindow().hide();
            Scene scene = null;
            System.out.println("Connected as "+userHandler.getClient().getRole() + " : "+userHandler.getClient().getNom()+" "+userHandler.getClient().getPrenom() + " | "+userHandler.getClient().getNumeroAbonnement());
            if (userHandler.getClient().isAdmin()){ // ADMIN
                root = FXMLLoader.load(RaPizzApplication.class.getResource("dashboardAdmin.fxml"));
                scene = new Scene(root, 500, 450);
                stage.setTitle("Dashboard ADMIN : " + userHandler.getClient().getPrenom() + " " + userHandler.getClient().getNom());
            }else { // CLIENT
                root = FXMLLoader.load(RaPizzApplication.class.getResource("dashboard.fxml"));
                scene = new Scene(root, 1000, 550);
                stage.setTitle("Dashboard de " + userHandler.getClient().getPrenom());
            }

            scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
            stage.setScene(scene);
            stage.show();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
