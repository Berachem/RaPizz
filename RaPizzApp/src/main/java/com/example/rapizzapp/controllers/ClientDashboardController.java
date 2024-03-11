package com.example.rapizzapp.controllers;

import com.example.rapizzapp.entities.Client;
import com.example.rapizzapp.utils.ClientService;
import com.example.rapizzapp.utils.UserHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ClientDashboardController {

    private ClientService customerService;
    private UserHandler userHandler = UserHandler.getInstance();

    @FXML
    private Label infoClient;

    // Supposons que vous avez un modèle pour Client, Commande et d'autres données nécessaires.

    public void initialize() {
        customerService = new ClientService();
        updateClientInformation();
        updateOrderHistory();
        showCurrentOrder();
    }

    private void updateClientInformation() {
        Client client = userHandler.getClient();
        infoClient.setText(
                "nom : "+client.getNom()+"\n"+
                "prenom : "+client.getPrenom()+"\n"+
                "abonnement : "+client.getNumeroAbonnement()+"\n"+
                "solde : "+client.getSolde()+"$"
        );

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
