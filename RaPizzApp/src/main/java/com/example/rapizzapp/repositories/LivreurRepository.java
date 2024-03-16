package com.example.rapizzapp.repositories;

import com.example.rapizzapp.entities.Livreur;
import com.example.rapizzapp.handlers.DatabaseHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
}
