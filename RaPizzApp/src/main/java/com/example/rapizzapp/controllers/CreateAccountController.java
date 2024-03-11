package com.example.rapizzapp.controllers;

import com.example.rapizzapp.entities.Client;
import com.example.rapizzapp.utils.ClientService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class CreateAccountController {

    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private TextField soldeField;

    private ClientService customerService;

    public void initialize() {
        customerService = new ClientService();
    }

    @FXML
    private void handleCreateAccount() {
        try {
            String nom = nomField.getText();
            String prenom = prenomField.getText();
            // numero est généré aléatoirement
            int numeroAbonnement = (int) (Math.random() * 1000000);
            int solde = Integer.parseInt(soldeField.getText());

            Client newClient = new Client(0, nom, prenom, numeroAbonnement, solde); // Assumer que l'ID est auto-généré ou ignoré pour l'insertion

            if (customerService.insertClient(newClient)) {
                showAlert("Succès", "Le compte a été créé avec succès. Votre numéro d'abonnement qui vous servira pour la connexion est : "+numeroAbonnement, Alert.AlertType.INFORMATION);
            } else {
                showAlert("Erreur", "Impossible de créer le compte.", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException nfe) {
            showAlert("Erreur", "Numéro d'abonnement et solde doivent être des valeurs numériques.", Alert.AlertType.ERROR);
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
