package com.example.rapizzapp.controllers;


import com.example.rapizzapp.RaPizzApplication;
import com.example.rapizzapp.entities.*;
import com.example.rapizzapp.handlers.UserHandler;
import com.example.rapizzapp.repositories.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;
import java.time.LocalDateTime;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;

public class CreateCommandController {

    //components
    @FXML
    public VBox form;
    public Label totalPrice;
    public TextField adresse;

    //data
    private UserHandler userHandler;
    private PizzaRepository pizzaRepository;
    private TailleRepository tailleRepository;
    private LivreurRepository livreurRepository;
    private VehiculeRepository vehiculeRepository;
    private CommandeRepository commandeRepository;

    private List<Pizza> pizzas;
    private List<Taille> tailles;
    private int pizzaNumber = 1;
    private double prixTotal;

    private boolean commandPassed = false;

    public void initialize() {
        //get repositories
        pizzaRepository = PizzaRepository.getInstance();
        tailleRepository = TailleRepository.getInstance();
        livreurRepository = LivreurRepository.getInstance();
        vehiculeRepository = VehiculeRepository.getInstance();
        commandeRepository = CommandeRepository.getInstance();
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

        //arrondir 2 chiffres après la virgule
        totalPrice = Math.round(totalPrice * 100.0) / 100.0;

        this.prixTotal = totalPrice;
        return prixTotal;
    }

    //fonction pour actualiser le prix d'une ligne en fonction de la pizza et de sa taille
    public void actualizePrice(Pizza selectedPizza, Taille selectedSize, Label correspondingLabel){
        Double prix = selectedPizza.getPrix();
        Double modificateur = 1 + Double.parseDouble(selectedSize.getModificateurPrix());
        Double realPrix = prix * modificateur;

        //arrondir 2 chiffres après la virgule
        realPrix = Math.round(realPrix * 100.0) / 100.0;

        //set du prix
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

            Pizza selectedPizza = pizzaSelector.getSelectionModel().getSelectedItem();
            Taille selectedTaille = tailleSelector.getSelectionModel().getSelectedItem();

            if(selectedPizza != null && selectedTaille != null){
                pizzas.put(selectedPizza,selectedTaille);
            }
        }
        if(pizzas.isEmpty()){
            showAlert("Erreur", "Veuillez renseigner au moins une pizza.", Alert.AlertType.ERROR);
            return;
        }

        //date arrivée/fin
        //délais entre 10 et 40 min
        long attente = 10L + (long) (Math.random() * 30L);
        LocalDateTime dateDebut = LocalDateTime.now();
        LocalDateTime dateFin = dateDebut.plus(Duration.ofMinutes(attente));

        //récupération Livreur/vehicule
        Vehicule vehicule;
        Livreur livreur = livreurRepository.getRandomAvailableLivreur();
        if(livreur == null){ //si aucun livreur dispo
            livreur = livreurRepository.getRandomLivreur();
            Commande lastCommand = commandeRepository.getLastCommandForLivreur(livreur.getIdLivreur());
            vehicule = lastCommand.getVehicule();
            //il faudra attendre que la commande soit livrée avant de repartir
            dateFin = lastCommand.getDateLivraison().plus(Duration.ofMinutes(attente));
        }else{
            vehicule = vehiculeRepository.getRandomAvailableVehicule();
        }

        //récupération de l'adresse
        String adresse = this.adresse.getText();
        if(adresse.isEmpty()){
            showAlert("Erreur", "Veuillez entrer votre adresse.", Alert.AlertType.ERROR);
            return;
        }

        //création de la commande
        Commande commande = new Commande();
        commande.setClient(client);
        commande.setPizzas(pizzas);
        commande.setDateCommande(dateDebut);
        commande.setDateLivraison(dateFin);
        commande.setLivreur(livreur);
        commande.setVehicule(vehicule);
        commande.setAdresseCommande(adresse);

        //insertion de la commande
        commandeRepository.insertCommande(commande);

        //information que la commande est passée
        commandPassed = true;

        //disparition de la page
        scene.getWindow().hide();
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public boolean isCommandPassed(){
        return commandPassed;
    }
}
