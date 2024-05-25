package com.example.rapizzapp.repositories;

import com.example.rapizzapp.entities.Pizza;
import com.example.rapizzapp.handlers.DatabaseHandler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PizzaRepository {

    private DatabaseHandler dbHandler;

    private static PizzaRepository pizzaRepository;

    private PizzaRepository() {
        dbHandler = DatabaseHandler.getInstance();
    }

    public static PizzaRepository getInstance(){
        if(pizzaRepository == null){
            pizzaRepository = new PizzaRepository();
        }
        return pizzaRepository;
    }

    public List<Pizza> getAllPizza() {
        List<Pizza> pizzas= new ArrayList<Pizza>();

        String sql = "SELECT * FROM Pizza";
        try (Connection conn = dbHandler.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                pizzas.add(new Pizza(rs.getInt("IdPizza"), rs.getString("libellePizza"), rs.getDouble("Prix"), "", new ArrayList<>()));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        // on récupère les ingrédients de chaque pizza (table Contient, jointure avec la table Ingrédient)
        for (Pizza pizza : pizzas) {
            List<String> ingredients = new ArrayList<>();
            String sqlIngredients = "SELECT * FROM Compose, Ingrédient WHERE IdPizza = " + pizza.getIdPizza() + " AND Compose.IdIngredient = Ingrédient.IdIngredient";
            try (Connection conn = dbHandler.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sqlIngredients)) {
                while (rs.next()) {
                    ingredients.add(rs.getString("libelleIngredient"));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            pizza.setIngredients(ingredients);
        }


        return pizzas;
    }

    public static List<Pizza> deepCopyPizzaList(List<Pizza> originalList) {
        List<Pizza> copiedList = new ArrayList<>();

        for (Pizza pizza : originalList) {
            // Copie profonde des ingrédients
            List<String> copiedIngredients = new ArrayList<>(pizza.getIngredients());

            // Création d'un nouvel objet Pizza avec les valeurs copiées
            Pizza copiedPizza = new Pizza(
                    pizza.getIdPizza(),
                    pizza.getLibellePizza(),
                    pizza.getPrix(),
                    pizza.getTaillePizza(),
                    copiedIngredients,
                    pizza.isGratuit()
            );

            copiedList.add(copiedPizza);
        }

        return copiedList;
    }
}
