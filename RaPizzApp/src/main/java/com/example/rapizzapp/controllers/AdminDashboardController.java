package com.example.rapizzapp.controllers;

import com.example.rapizzapp.RaPizzApplication;
import com.example.rapizzapp.handlers.UserHandler;
import com.example.rapizzapp.repositories.CommandeRepository;
import com.example.rapizzapp.repositories.StatsRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.util.Pair;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;
import java.sql.SQLException;



public class AdminDashboardController{

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

    private StatsRepository statsRepository;

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

    public void initialize() throws SQLException {
        statsRepository = StatsRepository.getInstance();
        updateStatistics();
    }

    private void updateStatistics() throws SQLException {

        // Récupération des informations
        String bestClient =  statsRepository.getBestClient();
        Pair<String, String> worstDeliveryPerson = statsRepository.getWorstDeliveryPerson();
        String worstDeliveryPersonName = worstDeliveryPerson.getKey();
        String vehicle = worstDeliveryPerson.getValue();
        String mostPopularPizza = statsRepository.getMostPopularPizza();
        String leastPopularPizza = statsRepository.getLeastPopularPizza();
        String favoriteIngredient = statsRepository.getFavoriteIngredient();

        // Mise à jour des labels
        bestClientLabel.setText(bestClient);
        worstDeliveryPersonLabel.setText(worstDeliveryPersonName);
        worstDeliveryPersonVehicleLabel.setText(vehicle);
        mostPopularPizzaLabel.setText(mostPopularPizza);
        leastPopularPizzaLabel.setText(leastPopularPizza);
        favoriteIngredientLabel.setText(favoriteIngredient);
    }
}
