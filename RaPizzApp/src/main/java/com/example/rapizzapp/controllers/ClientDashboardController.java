package com.example.rapizzapp.controllers;

import com.example.rapizzapp.entities.Client;
import com.example.rapizzapp.entities.Commande;
import com.example.rapizzapp.utils.ClientService;
import com.example.rapizzapp.utils.UserHandler;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ClientDashboardController {

    private ClientService customerService;
    private UserHandler userHandler = UserHandler.getInstance();

    @FXML
    private Label infoClient;


    // ========================================
    // HISTORIQUE DES COMMANDES
    // ========================================
    @FXML
    private TableView<Commande> tableHistoriqueCommandes;

    @FXML
    private TableColumn<Commande, Number> idCommandeColumn;

    @FXML
    private TableColumn<Commande, String> adresseColumn;

    @FXML
    private TableColumn<Commande, String> dateLivraisonColumn;

    @FXML
    private TableColumn<Commande, Number> montantColumn;

    // Supposons que vous avez un modèle pour Client, Commande et d'autres données nécessaires.

    public void initialize() {
        customerService = new ClientService();


        // Configure les colonnes
        idCommandeColumn.setCellValueFactory(new PropertyValueFactory<>("idCommande"));
        adresseColumn.setCellValueFactory(new PropertyValueFactory<>("adresseCommande"));
        dateLivraisonColumn.setCellValueFactory(new PropertyValueFactory<>("dateLivraison"));
        montantColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));


        // Charger les données du compte client
        updateClientInformation();
        // Charger les données de l'historique des commandes
        updateOrderHistory();
        showCurrentOrder();
    }

    private void updateClientInformation() {
        Client client = userHandler.getClient();
        infoClient.setText(
                "Nom : "+client.getNom()+ " "+client.getPrenom()+" | "+
                "Numéro d'abonnement : "+client.getNumeroAbonnement()+" | "+
                "Solde : "+client.getSolde()+" euros"
        );

        // Utilisez customerService pour obtenir les informations du client et les afficher
    }

    private void updateOrderHistory() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm");

        List<Commande> commandes = customerService.getClientOrderHistory(userHandler.getClient().getIdClient());
        // TODO : Mettre à jour les données de l'historique des commandes
        tableHistoriqueCommandes.setItems(FXCollections.observableArrayList(commandes));
        dateLivraisonColumn.setCellValueFactory(cellData -> {
            LocalDateTime date = cellData.getValue().getDateLivraison();
            String formattedDate = date != null ? date.format(formatter) : "";
            return new ReadOnlyStringWrapper(formattedDate);
        });
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
