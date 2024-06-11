package com.example.rapizzapp.repositories;

import com.example.rapizzapp.entities.*;
import com.example.rapizzapp.handlers.DatabaseHandler;
import javafx.util.Pair;

import java.sql.*;
import java.time.DayOfWeek;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatsRepository {

    private DatabaseHandler dbHandler;

    private static StatsRepository statsRepository;


    private StatsRepository() {
        dbHandler = DatabaseHandler.getInstance();
    }

    public static StatsRepository getInstance(){
        if(statsRepository == null){
            statsRepository = new StatsRepository();
        }
        return statsRepository;
    }

    private ClientRepository clientRepository = ClientRepository.getInstance();
    private CommandeRepository commandeRepository = CommandeRepository.getInstance();
    private LivreurRepository livreurRepository = LivreurRepository.getInstance();
    private PizzaRepository pizzaRepository = PizzaRepository.getInstance();

    private String[] worsteLivreur = livreurRepository.getWorstLivreur();

    // Retourne le chiffre d'affaire de l'entreprise (l'argent total des ventes)
    public double getSalesRevenue() throws SQLException {
        List<Commande> commandes = commandeRepository.getAllCommandes();
        double chiffreAffaires = 0.0;

        for (Commande commande : commandes){
            HashMap<Pizza, Taille> pizzas = commande.getPizzas();
            for (Map.Entry<Pizza, Taille> entry : pizzas.entrySet()) {
                Double modificateur = Double.parseDouble(entry.getValue().getModificateurPrix());
                Double realPrix = entry.getKey().getPrix() * modificateur;
                chiffreAffaires += (realPrix);
            }
        }
        return Math.round(chiffreAffaires * 100.0) / 100.0;
    }

    // Retourne un objet pair contenant le prénom+nom du client et son argent total dépensé
    public Pair<String, Double> getBestClient() {

        String[] client = clientRepository.getMontantDepenseParMeilleurClient();

        String name = client[0] + " " + client[1];

        return new Pair<>(name, Double.parseDouble(client[2]));
    }

    // Retourne le prix moyen d'une commande
    public double getAveragePrice() {

        return Math.round(commandeRepository.getPrixMoyenCommandes() * 100.0) / 100.0;
    }

    // Retourne un objet Pair contenant le prenom+nom du livreur et son véhicule le plus utilisé
    public Pair<String, String> getWorstDeliveryPerson() {

        return new Pair<>(worsteLivreur[1] + " " + worsteLivreur[0], worsteLivreur[2]);
    }

    // Retourne le nombre de livraisons en retard du pire livreur
    public int getLateDeliveriesCount() {
        return Integer.parseInt(worsteLivreur[3]);
    }

    // Retourne le nom de la pizza favorite
    public String getMostPopularPizza() {
        return pizzaRepository.getBestPizza();
    }

    // Retourne le nom de la pizza détestée
    public String getLeastPopularPizza() {
        return pizzaRepository.getWorstPizza();
    }

    // Retourne le nom de l'ingrédient favori
    public String getFavoriteIngredient() {
        return pizzaRepository.getMostOrderedIngredient();
    }

    // Retourne un objet DATE (de la classe java.sql) de la dernière commande effectuée
    public Timestamp getLastOrderDateTime() {
        return commandeRepository.getLastOrderDateTime();
    }

    // Retourne le nombre total de commandes effectuées
    public int getcommandAmount() {
        return commandeRepository.getcommandAmount();
    }

    // Retourne le temps de livraison moyen
    public Time getAverageDeliveryTime() {
        return commandeRepository.getOrderAverageTime();
    }

    // Retourne le jour de la semaine ayant eu le plus d'argent généré
    public DayOfWeek getBestWeekDay() {
        String strDay = commandeRepository.getBestWeekDay();
        return DayOfWeek.valueOf(strDay.toUpperCase());
    }

}

