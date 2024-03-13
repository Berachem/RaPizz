package com.example.rapizzapp.utils;

import com.example.rapizzapp.entities.Taille;
import com.example.rapizzapp.utils.DatabaseHandler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TailleRepository {

    private DatabaseHandler dbHandler;

    public TailleRepository() {
        dbHandler = new DatabaseHandler();
    }

    public List<Taille> getAllTailles() {
        List<Taille> tailles = new ArrayList<>();

        String sql = "SELECT * FROM Taille";
        try (Connection conn = dbHandler.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                tailles.add(new Taille(rs.getInt("idTaille"), rs.getString("LibelleTaille"), rs.getString("ModificateurPrix")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return tailles;
    }
}
