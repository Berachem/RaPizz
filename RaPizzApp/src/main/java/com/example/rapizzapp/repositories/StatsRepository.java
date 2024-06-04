package com.example.rapizzapp.repositories;

import com.example.rapizzapp.entities.*;
import com.example.rapizzapp.handlers.DatabaseHandler;
import javafx.util.Pair;

import java.sql.*;
import java.time.DayOfWeek;
import java.util.ArrayList;
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

    // TODO : ce sont ces fonctions que tu dois modifier Antonin !!

    // TODO : Retourne un objet pair contenant le prénom+nom du client et son argent total dépensé
    public Pair<String, Double> getBestClient() {
        String name = "Client A";
        Double moneySpent = 123.45;

        return new Pair<>(name, moneySpent);
    }

    // TODO : Retourne le prix moyen d'une commande
    public double getAveragePrice() { return  12.34; }

    // TODO : Retourne un objet Pair contenant le nom du livreur et son véhicule
    public Pair<String, String> getWorstDeliveryPerson() {
        String name = "Livreur B";
        String vehicule = "Véhicule X";

        return new Pair<>(name, vehicule);
    }

    // TODO : Retourne le nom de la pizza favorite
    public String getMostPopularPizza() {
        return "Pizza P";
    }

    // TODO : Retourne le nom de la pizza détestée
    public String getLeastPopularPizza() {
        return "Pizza Q";
    }

    // TODO : Retourne le nom de l'ingrédient favori
    public String getFavoriteIngredient() {
        return "Ingrédient I";
    }

    // TODO : Retourne un objet DATE (de la classe java.sql) de la dernière commande effectuée
    // TODO : je me suis dit que tu préfererais le java.sql.date pour tes requêtes
    // TODO : alors n'hésite pas à changer la DATE en java.util.date si ça t'arrange (l'autre a l'air deprecated)
    public Date getLastCommandDate() {
        Date emptyDate = new Date(0L);

        return emptyDate;
    }

    // TODO : Retourne le nombre total de commandes effectuées
    public int getcommandAmount() {
        return 1;
    }

    // TODO : Retourne le temps de livraison moyen
    public Time getAverageDeliveryTime() {
        Time averageDeliveryTime = new Time(0,12,34);
        return averageDeliveryTime;
    }

    // TODO : Retourne le jour de la semaine ayant eu le plus de commandes ou d'argent généré, comme tu souhaites
    // TODO : DayOfWeek est une enum, tu peux mettre en anglais car la traduction se fait chez moi ;))
    public DayOfWeek getBestWeekDay() {
        return DayOfWeek.MONDAY;
    }
}

