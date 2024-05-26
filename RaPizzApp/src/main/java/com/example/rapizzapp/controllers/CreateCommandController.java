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
import net.synedra.validatorfx.Check;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private ClientRepository clientRepository;

    private Client client;
    private HashMap<Integer,Pizza> pizzas = new HashMap<>();
    private List<Taille> tailles;
    private int prixTotal;
    private int compteurGratuit;

    private boolean commandPassed = false;

    public void initialize() {
        //get repositories
        pizzaRepository = PizzaRepository.getInstance();
        tailleRepository = TailleRepository.getInstance();
        livreurRepository = LivreurRepository.getInstance();
        vehiculeRepository = VehiculeRepository.getInstance();
        commandeRepository = CommandeRepository.getInstance();
        clientRepository = ClientRepository.getInstance();
        userHandler = UserHandler.getInstance();


        //getData
        client = userHandler.getClient();
        tailles = tailleRepository.getAllTailles();
        compteurGratuit = clientRepository.getNombrePizzasCommandeesParClient(client.getIdClient()) % 10;


        //setup the page
        for(Pizza pizza : pizzaRepository.getAllPizza()){

            //actualisation hashmap
            pizzas.put(pizza.getIdPizza(),pizza);

            //container
            HBox container = new HBox();
            container.setSpacing(10);

            //nom de pizza
            Label nomPizza = new Label("pizza "+pizza+" : ");

            //taille de la pizza
            ChoiceBox<Taille> selectTaille = new ChoiceBox<>();
            selectTaille.getItems().addAll(tailles);
            selectTaille.setId(pizza.getIdPizza()+"-taille");
            selectTaille.setOnAction(actionEvent -> {
                //récupération de tout ce qui correspond
                ChoiceBox<Taille> source = (ChoiceBox<Taille>) actionEvent.getSource();
                Integer numId = Integer.parseInt(source.getId().split("-")[0]);
                CheckBox checkPizza = (CheckBox) source.getScene().lookup("#"+numId+"-check");

                //si on décide de commander la pizza
                if(checkPizza.isSelected()){
                    Label correspondingLabel = (Label)  source.getScene().lookup("#"+numId+"-prix");

                    //récupération des objets
                    Taille selectedSize = source.getSelectionModel().getSelectedItem();
                    Pizza selectedPizza = pizzas.get(numId);

                    if(selectedPizza != null){
                        actualizePrice(selectedPizza,selectedSize,correspondingLabel);
                    }

                    totalPrice.setText("Total : "+getTotalPrice(source.getScene())+"€");
                }
            });

            //case à cocher
            CheckBox pizzaCheck = new CheckBox("ajouter");
            pizzaCheck.setId(pizza.getIdPizza()+"-check");
            pizzaCheck.setOnAction(actionEvent -> {
                CheckBox source = (CheckBox) actionEvent.getSource();
                Integer numId = Integer.parseInt(source.getId().split("-")[0]);
                Label correspondingLabel = (Label)  source.getScene().lookup("#"+numId+"-prix");
                if(source.isSelected()){
                    ChoiceBox<Taille> size = (ChoiceBox) source.getScene().lookup("#"+numId+"-taille");
                    Taille selectedSize = size.getSelectionModel().getSelectedItem();
                    Pizza selectedPizza = pizzas.get(numId);

                    if(selectedSize != null){
                        actualizePrice(selectedPizza,selectedSize,correspondingLabel);
                    }

                    totalPrice.setText("Total : "+getTotalPrice(source.getScene())+"€");
                }else{
                    correspondingLabel.setText("0€");
                    totalPrice.setText("Total : "+getTotalPrice(source.getScene())+"€");
                }
            });

            //label du prix
            Label prixPizza = new Label("0€");
            prixPizza.setId(pizza.getIdPizza()+"-prix");

            //ajout des balises
            container.getChildren().addAll(nomPizza,selectTaille,pizzaCheck,prixPizza);
            form.getChildren().add(container);
        }
    }

    //fonction pour récupérer le prix total des pizzas
    public int getTotalPrice(Scene scene){
        int totalPrice = 0;
        Label labelPrice;
        for(Map.Entry<Integer,Pizza> entry : pizzas.entrySet()){
            labelPrice = (Label) scene.lookup("#"+entry.getKey()+"-prix");
            CheckBox checkpizza = (CheckBox) scene.lookup("#"+entry.getKey()+"-check");
            if(checkpizza.isSelected()){
                totalPrice+= Integer.parseInt(labelPrice.getText().replace("€",""));
            }
        }

        this.prixTotal = totalPrice;
        return prixTotal;
    }

    //fonction pour actualiser le prix d'une ligne en fonction de la pizza et de sa taille
    public void actualizePrice(Pizza selectedPizza, Taille selectedSize, Label correspondingLabel){
        Double prix = selectedPizza.getPrix();
        Double modificateur = 1 + Double.parseDouble(selectedSize.getModificateurPrix());
        Double realPrix = prix * modificateur;

        //arrondir
        int roundedRealPrix = (int) Math.round(realPrix);

        //set du prix
        correspondingLabel.setText(roundedRealPrix+"€");
    }

    public void makeCommand(ActionEvent event){
        Button source = (Button) event.getSource();
        Scene scene = source.getScene();

        //récupération de l'adresse
        String adresse = this.adresse.getText();
        if(adresse.isEmpty()){
            showAlert("Erreur", "Veuillez entrer votre adresse.", Alert.AlertType.ERROR);
            return;
        }

        //actualisation du solde
        if(client.getSolde() < prixTotal){
            showAlert("Sold Insuffisant","Votre solde de compte est insuffisant !", Alert.AlertType.ERROR);
            return;
        }

        //récupération de toutes les commandes
        HashMap<Pizza,Taille> pizzaCommande = new HashMap<>();
        CheckBox pizzaCheck;
        ChoiceBox<Taille> tailleSelector;
        for(Map.Entry<Integer,Pizza> entry : pizzas.entrySet()){
            Integer id = entry.getKey();
            Pizza pizza = entry.getValue();

            pizzaCheck = (CheckBox) scene.lookup("#"+id+"-check");
            tailleSelector = (ChoiceBox) scene.lookup("#"+id+"-taille");

            Taille selectedTaille = tailleSelector.getSelectionModel().getSelectedItem();

            if(pizzaCheck.isSelected() && selectedTaille != null){
                compteurGratuit++;
                if(compteurGratuit%10 == 0){ //verification des pizzas gratuites
                    pizza.setGratuit(true);
                }
                pizzaCommande.put(pizza,selectedTaille);
            }
        }

        if(pizzaCommande.isEmpty()){
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

        //création de la commande
        Commande commande = new Commande();
        commande.setClient(client);
        commande.setPizzas(pizzaCommande);
        commande.setDateCommande(dateDebut);
        commande.setDateLivraison(dateFin);
        commande.setLivreur(livreur);
        commande.setVehicule(vehicule);
        commande.setAdresseCommande(adresse);

        //verification de la gratuité de la commande
        if(ChronoUnit.MINUTES.between(dateDebut, dateFin) > 30){
            commande.setGratuit(true);
        }else{
            client.setSolde((client.getSolde() - prixTotal));
            //on persiste le changement
            clientRepository.updateClient(client);
        }

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
