package com.example.rapizzapp.controllers;

import com.example.rapizzapp.RaPizzApplication;
import com.example.rapizzapp.entities.Client;
import com.example.rapizzapp.entities.Livreur;
import com.example.rapizzapp.handlers.UserHandler;
import com.example.rapizzapp.repositories.ClientRepository;
import com.example.rapizzapp.repositories.LivreurRepository;
import com.example.rapizzapp.repositories.StatsRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.util.Pair;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.DayOfWeek;
import java.util.Locale;


public class AdminDashboardController{

    @FXML
    public Label bestClientMoney;
    @FXML
    public Label averagePriceLabel;
    @FXML
    public Label lastCommandDateLabel;
    @FXML
    public Label commandAmountLabel;
    @FXML
    public Label averageDeliveryTimeLabel;
    @FXML
    public Label bestWeekDayLabel;
    @FXML
    private Label bestClientLabel;
    @FXML
    private Label worstDeliveryPersonLabel;
    @FXML
    private Label worstDeliveryPersonVehicleLabel;
    @FXML
    private Label mostPopularPizzaLabel;
    @FXML
    private Label leastPopularPizzaLabel;
    @FXML
    private Label favoriteIngredientLabel;
    @FXML
    private TextField clientNumAboTextField;
    @FXML
    private TextField livreurIdTextField;

    private StatsRepository statsRepository;
    private ClientRepository clientRepository;
    private LivreurRepository livreurRepository;

    @FXML
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

    public boolean isInteger( String input ) {
        try {
            Integer.parseInt( input );
            return true;
        }
        catch( Exception e ) {
            return false;
        }
    }

    public void initialize() throws SQLException {
        statsRepository = StatsRepository.getInstance();
        clientRepository = ClientRepository.getInstance();
        livreurRepository = LivreurRepository.getInstance();
        updateStatistics();
    }

    private void updateStatistics() throws SQLException {

        // Récupération des informations
        Pair<String, Double> bestClient =  statsRepository.getBestClient();
        String bestClientName = bestClient.getKey();
        double bestClientMoneySpent = bestClient.getValue();
        DayOfWeek bestWeekDay = statsRepository.getBestWeekDay();
        int commandAmount = statsRepository.getcommandAmount();
        double averagePrice = statsRepository.getAveragePrice();
        Date lastCommandDate = statsRepository.getLastCommandDate();
        Time averageDeliveryTime = statsRepository.getAverageDeliveryTime();
        Pair<String, String> worstDeliveryPerson = statsRepository.getWorstDeliveryPerson();
        String worstDeliveryPersonName = worstDeliveryPerson.getKey();
        String vehicle = worstDeliveryPerson.getValue();
        String mostPopularPizza = statsRepository.getMostPopularPizza();
        String leastPopularPizza = statsRepository.getLeastPopularPizza();
        String favoriteIngredient = statsRepository.getFavoriteIngredient();

        // Mise à jour des labels
        bestClientLabel.setText(bestClientName);
        bestClientMoney.setText(bestClientMoneySpent+" €");
        String frenchBestWeekDayText = bestWeekDay.getDisplayName(java.time.format.TextStyle.FULL, Locale.FRENCH);
        String capitalizeBestWeekDayText = Character.toUpperCase(frenchBestWeekDayText.charAt(0)) + frenchBestWeekDayText.substring(1);
        bestWeekDayLabel.setText(capitalizeBestWeekDayText);
        commandAmountLabel.setText(String.valueOf(commandAmount));
        averagePriceLabel.setText(averagePrice+" €");
        averageDeliveryTimeLabel.setText(averageDeliveryTime.toString());
        lastCommandDateLabel.setText(lastCommandDate.toGMTString()); // TODO : format à modifier si on change en java.util.sql
        worstDeliveryPersonLabel.setText(worstDeliveryPersonName);
        worstDeliveryPersonVehicleLabel.setText(vehicle);
        mostPopularPizzaLabel.setText(mostPopularPizza);
        leastPopularPizzaLabel.setText(leastPopularPizza);
        favoriteIngredientLabel.setText(favoriteIngredient);
    }

    @FXML
    private void searchClient() {
        String clientNumAbo = clientNumAboTextField.getText();
        if (!isInteger(clientNumAbo)) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez entrer un numéro d'abonnement de client valide");
        } else {
            Client client = clientRepository.getClientByNumeroAbonnement(Integer.parseInt(clientNumAbo));
            if (client == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Aucun client n'a été trouvé avec ce numéro d'abonnement");
            } else {
                openClientDetailsWindow(client);
            }
        }
    }

    private void openClientDetailsWindow(Client client) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/rapizzapp/ClientDetails.fxml"));
            Parent root = loader.load();

            ClientDetailsController controller = loader.getController();
            controller.setClientDetails(client);

            Stage stage = new Stage();
            stage.setTitle("Détails du Client");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de l'ouverture de la fenêtre de détails du client");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void searchLivreur(ActionEvent actionEvent) {
        String livreurId = livreurIdTextField.getText();
        if (!isInteger(livreurId)) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez entrer un ID de livreur valide");
        } else {
            Livreur livreur = livreurRepository.getLivreur(Integer.parseInt(livreurId));
            if (livreur == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Aucun livreur n'a été trouvé avec cet ID");
            } else {
                openLivreurDetailsWindow(livreur);
            }
        }
    }

    private void openLivreurDetailsWindow(Livreur livreur) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/rapizzapp/livreurDetails.fxml"));
            Parent root = loader.load();

            LivreurDetailsController controller = loader.getController();
            controller.setLivreurDetails(livreur);

            Stage stage = new Stage();
            stage.setTitle("Détails du Livreur");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de l'ouverture de la fenêtre de détails du livreur");
        }
    }


    public void addLivreur(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/rapizzapp/addLivreur.fxml"));
            Parent root = loader.load();


            Stage stage = new Stage();
            stage.setTitle("Ajouter un Livreur");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de l'ouverture de la fenêtre d'ajout du livreur");
        }
    }
}
