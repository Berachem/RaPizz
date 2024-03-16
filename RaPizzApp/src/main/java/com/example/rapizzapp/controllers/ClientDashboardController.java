package com.example.rapizzapp.controllers;

import com.example.rapizzapp.RaPizzApplication;
import com.example.rapizzapp.entities.Client;
import com.example.rapizzapp.entities.Commande;
import com.example.rapizzapp.repositories.ClientRepository;
import com.example.rapizzapp.handlers.UserHandler;
import com.example.rapizzapp.repositories.CommandeRepository;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.IOException;
import java.util.List;

public class ClientDashboardController {
    private CommandeRepository commandeRepository;
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
    private TableColumn<Commande, String> montantColumn;

    // Supposons que vous avez un modèle pour Client, Commande et d'autres données nécessaires.

    public void initialize() {
        commandeRepository = CommandeRepository.getInstance();

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

        List<Commande> commandes = commandeRepository.getClientOrderHistory(userHandler.getClient().getIdClient());
        // Mise à jour de l'historique des commandes
        tableHistoriqueCommandes.setItems(FXCollections.observableArrayList(commandes));
        dateLivraisonColumn.setCellValueFactory(cellData -> { // Formater la date de livraison
            LocalDateTime date = cellData.getValue().getDateLivraison();
            String formattedDate = date != null ? date.format(formatter) : "";
            return new ReadOnlyStringWrapper(formattedDate);
        });
        // rajout du symbole € pour le montant
        montantColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getMontant() + " €"));
    }

    private void showCurrentOrder() {
        // Utilisez customerService pour obtenir la commande en cours et l'afficher
    }

    @FXML
    private void createNewOrder(ActionEvent event) {
        Parent root;
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();

        try {
            // Hide this current window
            node.getScene().getWindow().hide();

            //load next window
            root = FXMLLoader.load(RaPizzApplication.class.getResource("createCommand.fxml"));
            stage.setTitle("Créer une commande");
            Scene scene = new Scene(root, 1000, 550);
            scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
            stage.setScene(scene);
            stage.show();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
