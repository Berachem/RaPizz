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
                pizzas.add(new Pizza(rs.getInt("IdPizza"), rs.getString("libellePizza"), rs.getDouble("Prix"), ""));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return pizzas;
    }
}
