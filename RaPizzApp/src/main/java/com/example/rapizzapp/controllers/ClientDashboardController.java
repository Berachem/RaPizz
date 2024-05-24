package com.example.rapizzapp.controllers;

import com.example.rapizzapp.RaPizzApplication;
import com.example.rapizzapp.entities.Client;
import com.example.rapizzapp.entities.Commande;
import com.example.rapizzapp.entities.Pizza;
import com.example.rapizzapp.repositories.ClientRepository;
import com.example.rapizzapp.handlers.UserHandler;
import com.example.rapizzapp.repositories.CommandeRepository;
import com.example.rapizzapp.repositories.PizzaRepository;
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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientDashboardController {
    public Label historyOrdersTitle;
    @FXML
    public VBox ingredientsContainer;
    private CommandeRepository commandeRepository;
    private UserHandler userHandler = UserHandler.getInstance();

    @FXML
    private Label infoClient;


    // ========================================
    // HISTORIQUE DES COMMANDES
    // ========================================
    @FXML
    private VBox orderHistoryContainer;

    // Supposons que vous avez un modèle pour Client, Commande et d'autres données nécessaires.

    public void initialize() throws SQLException {
        commandeRepository = CommandeRepository.getInstance();


        // Charger les données du compte client
        updateClientInformation();
        // Charger les données de l'historique des commandes
        updateOrderHistory();
        showCurrentOrder();
        updateIngredientsList();
    }

    private void updateClientInformation() {
        Client client = userHandler.getClient();
        infoClient.setText(
                "Nom : "+client.getNom()+ " "+client.getPrenom()+" \n"+
                "Numéro d'abonnement : "+client.getNumeroAbonnement()+" \n"+
                "Solde : "+client.getSolde()+" euros"
        );

        // Utilisez customerService pour obtenir les informations du client et les afficher
    }

    private void updateOrderHistory() throws SQLException {



        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm");

        List<Commande> commandes = commandeRepository.getClientOrderHistory(userHandler.getClient().getIdClient());
        orderHistoryContainer.getChildren().clear(); // Clear existing content
        historyOrdersTitle.setText("Historique des commandes (" + commandes.size() + ")");

        for (Commande commande : commandes) {
            VBox commandeBox = new VBox(5);
            commandeBox.setStyle("-fx-background-color: #ffffff; -fx-border-color: #cccccc; -fx-padding: 10;");

            Label idLabel = new Label("ID: " + commande.getIdCommande());
            Label montantLabel = new Label("Montant: " + commande.getMontant() + " €");
            Label dateLabel = new Label("Date de livraison: " + (commande.getDateLivraison() != null ? commande.getDateLivraison().format(formatter) : ""));
            Label adresseLabel = new Label("Adresse: " + commande.getAdresseCommande());


            Set<Pizza> pizzas = commande.getPizzas().keySet();

            String pizzasString = pizzas.stream()
                    .map(p -> "\n\t" + p.getLibellePizza() + " (" + p.getTaillePizza() + ") " )
                    .collect(Collectors.joining(", "));
            if (pizzasString.isEmpty()) {
                pizzasString = "Aucune pizza";
            }
            Label pizzasLabel = new Label("Pizzas: " + pizzasString);

            System.out.println("Pizzas: " + pizzas + " (" + commande.getIdCommande() + ")" );

            commandeBox.getChildren().addAll(idLabel, montantLabel, dateLabel, adresseLabel, pizzasLabel);
            orderHistoryContainer.getChildren().add(commandeBox);
        }
    }

    private void updateIngredientsList() {
        ingredientsContainer.getChildren().clear(); // Clear existing content

        List<Pizza> pizzas = PizzaRepository.getInstance().getAllPizza();

        if (pizzas != null) {
            for (Pizza pizza : pizzas) {
                VBox pizzaBox = new VBox(3);
                pizzaBox.setStyle("-fx-background-color: #ffffff; -fx-border-color: #cccccc; -fx-padding: 5;");

                Label pizzaNameLabel = new Label("Pizza: " + pizza.getLibellePizza() );
                Label ingredientsLabel = new Label("Ingrédients: \n\t" + String.join(", \n\t", pizza.getIngredients()));

                pizzaBox.getChildren().addAll(pizzaNameLabel, ingredientsLabel);
                ingredientsContainer.getChildren().add(pizzaBox);
            }
        }
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
