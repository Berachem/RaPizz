package com.example.rapizzapp.controllers;

import com.example.rapizzapp.utils.CustomerService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ClientDashboardController {

    private CustomerService customerService;

    @FXML
    private Label clientInformation;

    // Supposons que vous avez un modèle pour Client, Commande et d'autres données nécessaires.

    public void initialize() {
        customerService = new CustomerService();
        updateClientInformation();
        updateOrderHistory();
        showCurrentOrder();
    }

    private void updateClientInformation() {
        // Utilisez customerService pour obtenir les informations du client et les afficher
    }

    private void updateOrderHistory() {
        // Utilisez customerService pour obtenir l'historique des commandes du client et les afficher
    }

    private void showCurrentOrder() {
        // Utilisez customerService pour obtenir la commande en cours et l'afficher
    }

    @FXML
    private void createNewOrder() {
        // L'action pour créer une nouvelle commande sera implémentée ici
        // Pour le moment, vous pouvez commenter le code ou afficher un message.
    }
}
