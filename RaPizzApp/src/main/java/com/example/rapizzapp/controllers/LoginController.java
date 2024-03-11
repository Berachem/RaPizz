package com.example.rapizzapp.controllers;

import com.example.rapizzapp.HelloApplication;
import com.example.rapizzapp.entities.Client;
import com.example.rapizzapp.utils.CustomerService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    public TextField abonnementField;


    private CustomerService customerService;

    public void initialize() {
        customerService = new CustomerService();
    }

    @FXML
    public void loginAction() {
        String numAbo = abonnementField.getText();
        if (numAbo.isEmpty()) {
            showAlert("Erreur", "Veuillez entrer votre numéro d'abonnement.", Alert.AlertType.ERROR);
            return;
        }

        Client client = customerService.getClientByNumeroAbonnement(Integer.parseInt(numAbo));

        if (client != null) {
            passLogin();
        } else {
            showAlert("Erreur", "Numéro d'abonnement incorrect.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void createAccountAction(ActionEvent event) {
        Parent root;
        try {
            root = FXMLLoader.load(HelloApplication.class.getResource("createAccount.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Créer un compte");
            stage.setScene(new Scene(root, 450, 450));
            stage.show();
            // Hide this current window (if this is what you want)
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        // Ajoutez ici la logique pour créer un compte
    }

    @FXML
    public void passLogin(){
        Parent root;
        try {
            root = FXMLLoader.load(HelloApplication.class.getResource("dashboard.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Dashboard");
            stage.setScene(new Scene(root, 450, 450));
            stage.show();
            // Hide this current window (if this is what you want)
            //((Node)(event.getSource())).getScene().getWindow().hide();
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
