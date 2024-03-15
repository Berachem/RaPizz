package com.example.rapizzapp.controllers;


import com.example.rapizzapp.entities.Pizza;
import com.example.rapizzapp.entities.Taille;
import com.example.rapizzapp.utils.ClientRepository;
import com.example.rapizzapp.utils.PizzaRepository;
import com.example.rapizzapp.utils.TailleRepository;
import com.example.rapizzapp.utils.UserHandler;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.input.MouseEvent;
import java.util.List;

public class CreateCommandController {

    //components
    @FXML
    public VBox form;

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

    public void addPizza() {
        HBox container = new HBox();
        container.setSpacing(10);

        Label numPizza = new Label("pizza "+pizzaNumber+" : ");

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
                Double prix = selectedPizza.getPrix();
                Double modificateur = 1 + Double.parseDouble(selectedSize.getModificateurPrix());
                Double realPrix = prix * modificateur;
                correspondingLabel.setText(realPrix+"");
            }
        });


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
                Double prix = selectedPizza.getPrix();
                Double modificateur = 1 + Double.parseDouble(selectedSize.getModificateurPrix());
                Double realPrix = prix * modificateur;
                correspondingLabel.setText(realPrix+"");
            }
        });

        Label prixPizza = new Label("0€");
        prixPizza.setId(pizzaNumber+"-prix");

        container.getChildren().addAll(numPizza,selectPizzas,selectTaille,prixPizza);

        form.getChildren().add(container);

        pizzaNumber++;
    }

}
