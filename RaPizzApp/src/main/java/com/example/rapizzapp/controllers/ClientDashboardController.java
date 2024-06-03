package com.example.rapizzapp.controllers;


import com.example.rapizzapp.RaPizzApplication;
import com.example.rapizzapp.entities.Client;
import com.example.rapizzapp.entities.Commande;
import com.example.rapizzapp.entities.Pizza;
import com.example.rapizzapp.handlers.ImageLoaderTask;
import com.example.rapizzapp.repositories.ClientRepository;
import com.example.rapizzapp.handlers.UserHandler;
import com.example.rapizzapp.repositories.CommandeRepository;
import com.example.rapizzapp.repositories.PizzaRepository;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
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
        updateIngredientsList();
    }

    private void updateClientInformation() {
        Client client = userHandler.getClient();
        infoClient.setText(
                "Nom : "+client.getNom()+ " "+client.getPrenom()+" \n"+
                "Numéro d'abonnement : "+client.getNumeroAbonnement()+" \n"+
                "Solde : "+client.getSolde()+" euros (€)"
        );
        if(client.getSolde() < 0){
            infoClient.setStyle("-fx-border-color: red; -fx-border-width: 2px; -fx-padding: 10; -fx-border-radius: 5px;");
        }else{
            infoClient.setStyle("-fx-border-color: blue; -fx-border-width: 2px; -fx-padding: 10; -fx-border-radius: 5px;");
        }

    }

    private void updateOrderHistory() throws SQLException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm");

        List<Commande> commandes = commandeRepository.getClientOrderHistory(userHandler.getClient().getIdClient());
        orderHistoryContainer.getChildren().clear(); // Clear existing content
        historyOrdersTitle.setText("Mes commandes (" + commandes.size() + ")");

        // sort commandes by date DESC
        commandes.sort((c1, c2) -> c2.getDateCommande().compareTo(c1.getDateCommande()));

        LocalDateTime now = LocalDateTime.now();

        for (Commande commande : commandes) {
            VBox commandeBox = new VBox(5);

            // Vérifiez si la commande est en cours ou livrée
            if (commande.getDateLivraison() != null && commande.getDateLivraison().isAfter(now)) {
                commandeBox.setStyle("-fx-background-color: #d4ecd4; -fx-border-color: #66ff85; -fx-padding: 10;");
                 // En cours
            } else {
                commandeBox.setStyle("-fx-background-color: #c5c5c5; -fx-border-color: #151515; -fx-padding: 10;"); // Livrée
            }

            Label statusLabel = new Label(commande.getDateLivraison() != null && commande.getDateLivraison().isBefore(now) ? "Livrée" : "En cours");
            statusLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            Label idLabel = new Label("ID: " + commande.getIdCommande());
            Label montantLabel;
            if(commande.isGratuit()){
                montantLabel = new Label("Commande gratuite (livraison de plus de 30min)");
            }else{
                montantLabel = new Label("Montant: " + commande.getMontant() + " €");
            }
            Label dateLabel = new Label("Date de livraison: " + (commande.getDateLivraison() != null ? commande.getDateLivraison().format(formatter) : "N/A"));
            Label livreurLabel = new Label("Livreur: " + (commande.getLivreur() != null ? commande.getLivreur().getNom() + " " + commande.getLivreur().getPrenom() : "N/A"));
            Label adresseLabel = new Label("Adresse: " + commande.getAdresseCommande());

            Set<Pizza> pizzas = commande.getPizzas().keySet();
            String pizzasString = pizzas.stream()
                    .map(p -> "\n\t- " + p.getLibellePizza() + " (" + p.getTaillePizza() + ") ")
                    .collect(Collectors.joining(" "));
            if (pizzasString.isEmpty()) {
                pizzasString = "Aucune pizza";
            }
            Label pizzasLabel = new Label("Pizzas: " + pizzasString);

            commandeBox.getChildren().addAll(statusLabel, idLabel, montantLabel, dateLabel, livreurLabel, adresseLabel, pizzasLabel);
            orderHistoryContainer.getChildren().add(commandeBox);
        }
    }

    private void updateIngredientsList() {
        ingredientsContainer.getChildren().clear(); // Clear existing content

        List<Pizza> pizzas = PizzaRepository.getInstance().getAllPizza();

        if (pizzas != null) {
            for (Pizza pizza : pizzas) {
                HBox bigBox = new HBox(2);
                bigBox.setAlignment(Pos.CENTER_LEFT);

                VBox pizzaBox = new VBox(3);
                pizzaBox.setStyle("-fx-background-color: #eeceb2; -fx-border-color: #522b00; -fx-padding: 5; -fx-border-radius: 5px;");
                pizzaBox.setMinSize(150, 150);
                pizzaBox.setPrefSize(150, 150);
                pizzaBox.setMaxSize(150, 150);

                Label pizzaNameLabel = new Label("Pizza: " + pizza.getLibellePizza());
                pizzaNameLabel.setStyle("-fx-font-weight: bold;");
                Label ingredientsLabel = new Label("Ingrédients: \n\t- " + String.join(" \n\t- ", pizza.getLibelleIngredients()));

                pizzaBox.getChildren().addAll(pizzaNameLabel, ingredientsLabel);

                VBox imageBox = new VBox(3);
                imageBox.setAlignment(Pos.CENTER);

                String imageURL = pizza.getImagePizza();
                ImageView imageView = new ImageView();

                imageView.setFitWidth(150);
                imageView.setFitHeight(150);
                imageView.setPreserveRatio(true);

                // Load image asynchronously
                ImageLoaderTask task = new ImageLoaderTask(imageURL);
                task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        Image image = task.getValue();
                        imageView.setImage(image);
                    }
                });
                task.setOnFailed(event -> {
                    Label errorLabel = new Label("Image non disponible");
                    errorLabel.setWrapText(true);
                    errorLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
                    errorLabel.setMaxWidth(150);
                    imageBox.getChildren().add(errorLabel);
                });

                Thread thread = new Thread(task);
                thread.setDaemon(true);
                thread.start();

                imageBox.getChildren().add(imageView);
                bigBox.getChildren().addAll(pizzaBox, imageBox);
                ingredientsContainer.getChildren().add(bigBox);
            }
        }
    }
    private void showCurrentOrder() {
        int clientId = userHandler.getClient().getIdClient();
        Commande commande = commandeRepository.getLastCommandeFromClient(clientId);
        showAlert("Récapitulatif",commande.toString(), Alert.AlertType.INFORMATION);
    }

    @FXML
    private void createNewOrder(ActionEvent event) {
        Parent root;
        Node node = (Node) event.getSource();
        Stage stage = new Stage(); //pop une nouvelle fenetre pas changer la courante

        try {
            // Hide this current window
            //node.getScene().getWindow().hide();

            //load next window
            FXMLLoader loader = new FXMLLoader(RaPizzApplication.class.getResource("createCommand.fxml"));
            root = loader.load();
            CreateCommandController controller = loader.getController();
            stage.setTitle("Créer une commande");
            Scene scene = new Scene(root);
            scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
            stage.setScene(scene);
            stage.show();

            //actions à effectuer quand la scène de commande se ferme
            stage.setOnHiding((e)->{
                System.out.println("fenetre close");
                        try {
                            initialize();
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }

                        if(controller.isCommandPassed()){
                    showCurrentOrder();
                }
            }
            );
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

    public void logout(ActionEvent actionEvent) {
        System.out.println("Disconnected " + UserHandler.getInstance().getClient().getRole().toLowerCase() + " : "+UserHandler.getInstance().getClient().getNom()+" "+UserHandler.getInstance().getClient().getPrenom() + " | "+UserHandler.getInstance().getClient().getNumeroAbonnement());
        UserHandler.resetLogin();
        backToLogin(actionEvent);
    }

    @FXML
    public void backToLogin(ActionEvent event)  {
        Parent root;
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();

        try {
            // Hide this current window
            node.getScene().getWindow().hide();
            Scene scene = null;
            root = FXMLLoader.load(RaPizzApplication.class.getResource("login.fxml"));

            scene = new Scene(root, 320, 440);

            scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
            stage.setTitle("Bienvenue sur Rapizz ! 🍕");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openAddBalancePage(ActionEvent event) {
        Parent root;
        Stage stage = new Stage();

        try {
            FXMLLoader loader = new FXMLLoader(RaPizzApplication.class.getResource("add_balance.fxml"));
            root = loader.load();
            stage.setTitle("Ajouter du solde");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            //actions à effectuer quand la scène d'ajout de solde se ferme
            stage.setOnHiding((e) -> {
                updateClientInformation();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportPizzaMenu(ActionEvent actionEvent) {
        PizzaRepository pizzaRepository = PizzaRepository.getInstance();
        List<Pizza> pizzas = pizzaRepository.getAllPizza();
        if (pizzas != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Liste des pizzas disponibles : \n");
            for (Pizza pizza : pizzas) {
                sb.append(pizza.getLibellePizza());
                for (String ingredient : pizza.getLibelleIngredients()) {
                    sb.append("\n\t- ").append(ingredient);
                }
                sb.append("\n");
            }


            // Afficher le menu
            showAlert("Menu des pizzas", sb+"\n\nLe menu a été exporté avec succès dans le fichier menu_pizzas.txt !", Alert.AlertType.INFORMATION);

            // Enregistrer le menu dans un fichier texte
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("menu_pizzas.txt"))) {
                writer.write(sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erreur", "Une erreur s'est produite lors de l'enregistrement du menu.", Alert.AlertType.ERROR);
            }

        } else {
            showAlert("Menu des pizzas", "Aucune pizza disponible", Alert.AlertType.INFORMATION);
        }
    }
}
