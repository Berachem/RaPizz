package com.example.rapizzapp.repositories;

import com.example.rapizzapp.entities.*;
import com.example.rapizzapp.handlers.DatabaseHandler;
import javafx.util.Pair;

import java.sql.*;
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

    // Retourne le nom du client
    public String getBestClient() {
        return "Client A";
    }

    // Retourne un objet Pair contenant le nom du livreur et son véhicule
    public Pair<String, String> getWorstDeliveryPerson() {
        String name = "Livreur B";
        String vehicule = "Véhicule X";
        return new Pair<>(name, vehicule);
    }

    // Retourne le nom de la pizza
    public String getMostPopularPizza() {
        return "Pizza P";
    }

    // Retourne le nom de la pizza
    public String getLeastPopularPizza() {
        return "Pizza Q";
    }

    // Retourne le nom de l'ingrédient
    public String getFavoriteIngredient() {
        return "Ingrédient I";
    }
}
