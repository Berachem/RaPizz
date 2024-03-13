package com.example.rapizzapp.utils;

import com.example.rapizzapp.entities.Client;
import com.example.rapizzapp.entities.Pizza;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PizzaRepository {

    private DatabaseHandler dbHandler;

    public PizzaRepository() {
        dbHandler = new DatabaseHandler();
    }

    public List<Pizza> getAllPizza() {
        List<Pizza> pizzas= new ArrayList<Pizza>();

        String sql = "SELECT * FROM Pizza";
        try (Connection conn = dbHandler.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                pizzas.add(new Pizza(rs.getInt("IdPizza"), rs.getString("libellePizza"), rs.getDouble("Prix")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return pizzas;
    }
}
