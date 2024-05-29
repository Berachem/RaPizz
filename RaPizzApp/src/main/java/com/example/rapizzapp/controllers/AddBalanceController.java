package com.example.rapizzapp.controllers;
import com.example.rapizzapp.handlers.UserHandler;
import com.example.rapizzapp.repositories.ClientRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddBalanceController {
    @FXML
    private TextField amountField;

    private ClientRepository clientRepository;

    @FXML
    private void addBalance(ActionEvent event) {
        String amountText = amountField.getText();
        try {
            double amount = Double.parseDouble(amountText);
            UserHandler userHandler = UserHandler.getInstance();
            userHandler.getClient().addSolde(amount);
            clientRepository = ClientRepository.getInstance();
            clientRepository.addSolde(userHandler.getClient().getIdClient(), Integer.parseInt(amountText));

            // Fermer la fenêtre après ajout de solde
            Stage stage = (Stage) amountField.getScene().getWindow();
            stage.close();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer un montant valide.", Alert.AlertType.ERROR);
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
