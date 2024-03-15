package com.example.rapizzapp.controllers;


import com.example.rapizzapp.entities.Client;
import com.example.rapizzapp.entities.Commande;
import com.example.rapizzapp.entities.Pizza;
import com.example.rapizzapp.entities.Taille;
import com.example.rapizzapp.utils.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.input.MouseEvent;
import java.time.LocalDateTime;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CreateCommandController {

    //components
    @FXML
    public VBox form;
    public Label totalPrice;

    //data
    private UserHandler userHandler;
    private ClientRepository clientRepository;
    private PizzaRepository pizzaRepository;
    private TailleRepository tailleRepository;
    private List<Pizza> pizzas;
    private List<Taille> tailles;
    private int pizzaNumber = 1;
    private double prixTotal;

    public void initialize() {
        //get repositories
        clientRepository = new ClientRepository();
        pizzaRepository = new PizzaRepository();
        tailleRepository = new TailleRepository();
        userHandler = UserHandler.getInstance();

        //getData
        pizzas = pizzaRepository.getAllPizza();
        tailles = tailleRepository.getAllTailles();

        //set a first choice
        addPizza();
    }

    //ajout des champs pour selectionner une pizza suplémentaire
    public void addPizza() {
        HBox container = new HBox();
        container.setSpacing(10);

        //numéro de pizza
        Label numPizza = new Label("pizza "+pizzaNumber+" : ");

        //select type pizza
        ChoiceBox<Pizza> selectPizzas = new ChoiceBox<>();
        selectPizzas.getItems().addAll(pizzas);
        selectPizzas.setId(pizzaNumber+"-pizza");
        selectPizzas.setOnAction(actionEvent -> {
            //récupération de tout ce qui correspond
            ChoiceBox<Pizza> source = (ChoiceBox<Pizza>) actionEvent.getSource();
            String numId = source.getId().split("-")[0];
            ChoiceBox<Taille> correspondingSize = (ChoiceBox<Taille>) source.getScene().lookup("#"+numId+"-taille");
            Label correspondingLabel = (Label)  source.getScene().lookup("#"+numId+"-prix");

            //récupération des objets
            Pizza selectedPizza = source.getSelectionModel().getSelectedItem();
            Taille selectedSize = correspondingSize.getSelectionModel().getSelectedItem();

            if(selectedSize != null){
                actualizePrice(selectedPizza,selectedSize,correspondingLabel);
            }

            totalPrice.setText("Total : "+getTotalPrice(source.getScene())+"€");
        });

        //select taille pizza
        ChoiceBox<Taille> selectTaille = new ChoiceBox<>();
        selectTaille.getItems().addAll(tailles);
        selectTaille.setId(pizzaNumber+"-taille");
        selectTaille.setOnAction(actionEvent -> {
            //récupération de tout ce qui correspond
            ChoiceBox<Taille> source = (ChoiceBox<Taille>) actionEvent.getSource();
            String numId = source.getId().split("-")[0];
            ChoiceBox<Pizza> correspondingPizza = (ChoiceBox<Pizza>) source.getScene().lookup("#"+numId+"-pizza");
            Label correspondingLabel = (Label)  source.getScene().lookup("#"+numId+"-prix");

                    //récupération des objets
            Taille selectedSize = source.getSelectionModel().getSelectedItem();
            Pizza selectedPizza = correspondingPizza.getSelectionModel().getSelectedItem();

            if(selectedPizza != null){
                actualizePrice(selectedPizza,selectedSize,correspondingLabel);
            }

            totalPrice.setText("Total : "+getTotalPrice(source.getScene())+"€");
        });

        //label du prix
        Label prixPizza = new Label("0€");
        prixPizza.setId(pizzaNumber+"-prix");

        //ajout des balises
        container.getChildren().addAll(numPizza,selectPizzas,selectTaille,prixPizza);
        form.getChildren().add(container);

        //incrémentation du nombre pour le prochain tour
        pizzaNumber++;
    }

    //fonction pour récupérer le prix total des pizzas
    public Double getTotalPrice(Scene scene){
        Double totalPrice = 0.0;
        Label labelPrice;
        for(int i=1;i<pizzaNumber;i++){
            labelPrice = (Label) scene.lookup("#"+i+"-prix");
            totalPrice+= Double.parseDouble(labelPrice.getText().replace("€",""));
        }
        this.prixTotal = totalPrice;
        return prixTotal;
    }

    //fonction pour actualiser le prix d'une ligne en fonction de la pizza et de sa taille
    public void actualizePrice(Pizza selectedPizza, Taille selectedSize, Label correspondingLabel){
        Double prix = selectedPizza.getPrix();
        Double modificateur = 1 + Double.parseDouble(selectedSize.getModificateurPrix());
        Double realPrix = prix * modificateur;
        correspondingLabel.setText(realPrix+"");
    }

    public void makeCommand(ActionEvent event){
        Button source = (Button) event.getSource();
        Scene scene = source.getScene();

        //récupération du User
        Client client = userHandler.getClient();

        //récupération de toutes les commandes
        HashMap<Pizza,Taille> pizzas = new HashMap<>();
        ChoiceBox<Pizza> pizzaSelector;
        ChoiceBox<Taille> tailleSelector;
        for(int i=1;i<pizzaNumber;i++){
            pizzaSelector = (ChoiceBox) scene.lookup("#"+i+"-pizza");
            tailleSelector = (ChoiceBox) scene.lookup("#"+i+"-taille");
            pizzas.put(pizzaSelector.getSelectionModel().getSelectedItem(),tailleSelector.getSelectionModel().getSelectedItem());
        }

        //date arrivée/fin
        //délais entre 10 et 40 min
        long attente = 10L + (long) (Math.random() * 30L);
        LocalDateTime dateDebut = LocalDateTime.now();
        LocalDateTime dateFin = dateDebut.plus(Duration.ofMinutes(attente));


        //création de la commande
        Commande commande = new Commande();
        commande.setIdClient(client.getIdClient());
        commande.setPizzas(pizzas);
        commande.setDateCommande(dateDebut);
        commande.setDateLivraison(dateFin);

        //TODO : livreur,vehicule,adresse

    }
}
