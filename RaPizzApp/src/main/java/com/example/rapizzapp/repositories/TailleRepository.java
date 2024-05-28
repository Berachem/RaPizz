package com.example.rapizzapp.repositories;

import com.example.rapizzapp.entities.Livreur;
import com.example.rapizzapp.entities.Taille;
import com.example.rapizzapp.handlers.DatabaseHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TailleRepository {

    private DatabaseHandler dbHandler;

    private static TailleRepository tailleRepository;

    private TailleRepository() {
        dbHandler = DatabaseHandler.getInstance();
    }

    public static TailleRepository getInstance(){
        if(tailleRepository == null){
            tailleRepository = new TailleRepository();
        }
        return tailleRepository;
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

    public boolean insertTaille(Taille taille) {
        String sql = "INSERT INTO Taille(LibelleTaille, ModificateurPrix) VALUES (?, ?)";
        try (Connection conn = dbHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, taille.getLibelleTaille());
            pstmt.setString(2, taille.getModificateurPrix());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public boolean update(Taille taille) {
        String sql = "UPDATE Taille SET LibelleTaille = ?, ModificateurPrix = ? WHERE idTaille = ?";
        try (Connection conn = dbHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, taille.getLibelleTaille());
            pstmt.setString(2, taille.getModificateurPrix());
            pstmt.setInt(3, taille.getIdTaille());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public boolean deleteTaille(Taille taille) {
        String sql = "DELETE FROM Livreur WHERE idTaille = ?";
        try (Connection conn = dbHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, taille.getIdTaille());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
}
