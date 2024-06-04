package com.example.rapizzapp.controllers;

import com.example.rapizzapp.RaPizzApplication;
import com.example.rapizzapp.entities.Client;
import com.example.rapizzapp.handlers.UserHandler;
import com.example.rapizzapp.repositories.ClientRepository;
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

public class CreateAccountController extends LoginController {

    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private TextField soldeField;

    private ClientRepository customerService;

    public void initialize() {
        customerService = ClientRepository.getInstance();
    }

    @FXML
    private void handleCreateAccount(ActionEvent event) {
        try {
            String nom = nomField.getText();
            String prenom = prenomField.getText();
            // numero est généré aléatoirement
            int numeroAbonnement = (int) (Math.random() * 1000000);
            int solde = Integer.parseInt(soldeField.getText());

            Client newClient = new Client(0, nom, prenom, numeroAbonnement, solde, "USER"); // Assumer que l'ID est auto-généré ou ignoré pour l'insertion

            if (customerService.insertClient(newClient)) {
                showAlert("Succès", "Le compte a été créé avec succès. Votre numéro d'abonnement qui vous servira pour la connexion est : "+numeroAbonnement, Alert.AlertType.INFORMATION);

                UserHandler userHandler = UserHandler.getInstance();
                userHandler.setClient(newClient);
                passLogin(event);
            } else {
                showAlert("Erreur", "Impossible de créer le compte.", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException nfe) {
            showAlert("Erreur", "Le solde doit être une valeur numérique.", Alert.AlertType.ERROR);
        }
    }


    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


    public void handleBack(ActionEvent event) {
        Parent root;
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();

        try {
            // Hide this current window
            node.getScene().getWindow().hide();

            //load next window
            root = FXMLLoader.load(RaPizzApplication.class.getResource("login.fxml"));
            stage.setTitle("Bienvenue sur RapizzApp ! 🍕");
            Scene scene = new Scene(root, 320, 440);
            scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
            stage.setScene(scene);
            stage.show();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
