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

    // TODO : ce sont ces fonctions que tu dois modifier Antonin !!

    // TODO : Retourne le nom du client
    public String getBestClient() {
        return "Client A";
    }

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
}
