package com.example.rapizzapp.repositories;

import com.example.rapizzapp.entities.*;
import com.example.rapizzapp.handlers.DatabaseHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.*;

public class LivreurRepository {

    private DatabaseHandler dbHandler;

    private static LivreurRepository livreurRepository;

    private LivreurRepository() {
        dbHandler = DatabaseHandler.getInstance();
    }

    public static LivreurRepository getInstance(){
        if(livreurRepository == null){
            livreurRepository = new LivreurRepository();
        }
        return livreurRepository;
    }

    public List<Livreur> getAllLivreurs() {
        List<Livreur> livreurs = new ArrayList<>();

        String sql = "SELECT * FROM Livreur";
        try (Connection conn = dbHandler.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int idLivreur = rs.getInt("IdLivreur");
                String nom = rs.getString("Nom");
                String prenom = rs.getString("Prenom");
                // Créer un objet Livreur et l'ajouter à la liste
                Livreur livreur = new Livreur(idLivreur, nom, prenom);
                livreurs.add(livreur);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return livreurs;
    }

    public Livreur getRandomLivreur() {
        Livreur livreur = null;

        String sql = "SELECT * FROM Livreur ORDER BY RAND() LIMIT 1";
        try (Connection conn = dbHandler.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                int idLivreur = rs.getInt("IdLivreur");
                String nom = rs.getString("Nom");
                String prenom = rs.getString("Prenom");
                livreur = new Livreur(idLivreur, nom, prenom);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return livreur;
    }

    public Livreur getRandomAvailableLivreur() {
        Livreur livreur = null;

        // Requête pour récupérer les livreurs disponibles (non associés à des commandes en cours de livraison)
        String sql = "SELECT * FROM Livreur " +
                "WHERE IdLivreur NOT IN " +
                "(SELECT IdLivreur FROM Commande WHERE DateLivraison > NOW()) " +
                "ORDER BY RAND() LIMIT 1";

        try (Connection conn = dbHandler.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                int idLivreur = rs.getInt("IdLivreur");
                String nom = rs.getString("Nom");
                String prenom = rs.getString("Prenom");
                livreur = new Livreur(idLivreur, nom, prenom);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return livreur;
    }

    public Livreur getLivreur(int idLivreur) {
        Livreur livreur = null;

        String sql = "SELECT * FROM Livreur WHERE IdLivreur = ?";
        try (Connection conn = dbHandler.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idLivreur);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String nom = rs.getString("Nom");
                    String prenom = rs.getString("Prenom");
                    livreur = new Livreur(idLivreur, nom, prenom);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return livreur;
    }

    public String[] getWorstLivreur(){
        String sql = "WITH LivraisonTemps AS (" +
                "    SELECT " +
                "        L.IdLivreur," +
                "        L.Nom," +
                "        L.Prenom," +
                "        V.Nom AS NomVehicule," +
                "        TIMESTAMPDIFF(MINUTE, Co.DateCommande, Co.DateLivraison) AS TempsLivraison, " +
                "        CASE WHEN TIMESTAMPDIFF(MINUTE, Co.DateCommande, Co.DateLivraison) > 30 THEN 1 ELSE 0 END AS Retard " +
                "    FROM " +
                "        Livreur L" +
                "    JOIN " +
                "        Commande Co ON L.IdLivreur = Co.IdLivreur " +
                "    JOIN " +
                "        Vehicule V ON Co.IdVehicule = V.IdVehicule " +
                "), " +
                "LivreurAvecTempsMax AS (" +
                "    SELECT " +
                "        L.IdLivreur," +
                "        L.Nom," +
                "        L.Prenom," +
                "        AVG(TIMESTAMPDIFF(MINUTE, Co.DateCommande, Co.DateLivraison)) AS TempsMoyenLivraison," +
                "        SUM(CASE WHEN TIMESTAMPDIFF(MINUTE, Co.DateCommande, Co.DateLivraison) > 30 THEN 1 ELSE 0 END) AS NombreRetards," +
                "        RANK() OVER (ORDER BY AVG(TIMESTAMPDIFF(MINUTE, Co.DateCommande, Co.DateLivraison)) DESC) AS Rang " +
                "    FROM " +
                "        Livreur L " +
                "    JOIN " +
                "        Commande Co ON L.IdLivreur = Co.IdLivreur " +
                "    GROUP BY " +
                "        L.IdLivreur, L.Nom, L.Prenom" +
                "), " +
                "LivraisonStatistiques AS (" +
                "    SELECT" +
                "        L.IdLivreur," +
                "        V.Nom AS NomVehicule," +
                "        COUNT(*) AS UtilisationVehicule " +
                "    FROM " +
                "        Livreur L" +
                "    JOIN " +
                "        Commande Co ON L.IdLivreur = Co.IdLivreur " +
                "    JOIN " +
                "        Vehicule V ON Co.IdVehicule = V.IdVehicule " +
                "    WHERE " +
                "        L.IdLivreur = (SELECT IdLivreur FROM LivreurAvecTempsMax WHERE Rang = 1) " +
                "    GROUP BY " +
                "        L.IdLivreur, V.Nom" +
                ")" +
                "SELECT " +
                "    L.Nom," +
                "    L.Prenom," +
                "    V.NomVehicule," +
                "    L.TempsMoyenLivraison," +
                "    L.NombreRetards " +
                "FROM" +
                "    LivreurAvecTempsMax L " +
                "JOIN" +
                "    (SELECT NomVehicule FROM LivraisonStatistiques ORDER BY UtilisationVehicule DESC LIMIT 1) V " +
                "ON" +
                "    L.IdLivreur = (SELECT IdLivreur FROM LivreurAvecTempsMax WHERE Rang = 1)" +
                "WHERE" +
                "    L.Rang = 1";
        try (Connection conn = dbHandler.getConnection();
             Statement stmt = conn.createStatement()) {

            try (ResultSet rs = stmt.executeQuery(sql)) {
                if (rs.next()) {
                    String nom = rs.getString("Nom");
                    String prenom = rs.getString("Prenom");
                    String nomVehicle = rs.getString("NomVehicule");
                    String nombreRetards = rs.getString("NombreRetards");
                    String[] result = {nom, prenom, nomVehicle, nombreRetards};
                    return result;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        String[] result = {};
        return result;
    }

    public boolean insertLivreur(Livreur livreur) {
        String sql = "INSERT INTO Livreur(Nom, Prenom) VALUES (?, ?)";
        try (Connection conn = dbHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, livreur.getNom());
            pstmt.setString(2, livreur.getPrenom());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public boolean updateLivreur(Livreur livreur) {
        String sql = "UPDATE Livreur SET Nom = ?, Prenom = ? WHERE IdLivreur = ?";
        try (Connection conn = dbHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, livreur.getNom());
            pstmt.setString(2, livreur.getPrenom());
            pstmt.setInt(3, livreur.getIdLivreur());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public boolean deleteLiveur(Livreur livreur) {
        String sql = "DELETE FROM Livreur WHERE IdLivreur = ?";
        try (Connection conn = dbHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, livreur.getIdLivreur());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
}
