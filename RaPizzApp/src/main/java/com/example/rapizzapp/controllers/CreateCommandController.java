package com.example.rapizzapp.controllers;

import com.example.rapizzapp.entities.Client;
import com.example.rapizzapp.entities.Pizza;
import com.example.rapizzapp.entities.Taille;
import com.example.rapizzapp.utils.ClientRepository;
import com.example.rapizzapp.utils.PizzaRepository;
import com.example.rapizzapp.utils.TailleRepository;
import com.example.rapizzapp.utils.UserHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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
        selectPizzas.setId(numPizza+"-pizza");

        ChoiceBox<Taille> selectTaille = new ChoiceBox<>();
        selectTaille.getItems().addAll(tailles);
        selectTaille.setId(numPizza+"-taille");

        Label prixPizza = new Label("0€");
        prixPizza.setId(numPizza+"-prix");

        container.getChildren().addAll(numPizza,selectPizzas,selectTaille,prixPizza);

        form.getChildren().add(container);

        pizzaNumber++;
    }

}
