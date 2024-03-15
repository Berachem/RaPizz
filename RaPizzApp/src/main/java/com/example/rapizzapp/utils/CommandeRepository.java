package com.example.rapizzapp.utils;

import com.example.rapizzapp.entities.Commande;
import com.example.rapizzapp.entities.Pizza;
import com.example.rapizzapp.entities.Taille;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandeRepository {

    private DatabaseHandler dbHandler;

    public CommandeRepository() {
        dbHandler = new DatabaseHandler();
    }

    public List<Commande> getAllCommandes() {
        List<Commande> commandes = new ArrayList<>();

        String sql = "SELECT * FROM Commande";
        try (Connection conn = dbHandler.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Commande commande = new Commande();
                commande.setIdCommande(rs.getInt("idCommande"));
                commande.setAdresseCommande(rs.getString("adresseCommande"));
                commande.setDateCommande(rs.getTimestamp("DateCommande").toLocalDateTime());
                commande.setDateLivraison(rs.getTimestamp("DateLivraison").toLocalDateTime());

                // Récupération des pizzas associées à la commande
                HashMap<Pizza, Taille> pizzas = getPizzasByCommandeId(conn, commande.getIdCommande());
                commande.setPizzas(pizzas);

                commandes.add(commande);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return commandes;
    }

    private HashMap<Pizza,Taille> getPizzasByCommandeId(Connection conn, int commandeId) throws SQLException {
        HashMap<Pizza,Taille> pizzas = new HashMap<>();

        String sql = "SELECT p.*,t.* FROM Pizza p INNER JOIN Contient c ON p.IdPizza = c.IdPizza NATURAL JOIN Taille t WHERE c.idCommande = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, commandeId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Pizza pizza = new Pizza();
                pizza.setIdPizza(rs.getInt("IdPizza"));
                pizza.setLibellePizza(rs.getString("LibellePizza"));
                pizza.setPrix(rs.getDouble("Prix"));

                Taille taille = new Taille();
                taille.setIdTaille(rs.getInt("IdTaille"));
                taille.setLibelleTaille(rs.getString("LibelleTaille"));

                pizzas.put(pizza,taille);
            }
        }

        return pizzas;
    }

    public void insertCommande(Commande commande) {
        String commandeSql = "INSERT INTO Commande (adresseCommande, DateCommande, DateLivraison, IdClient, IdLivreur, IdVehicule) VALUES (?, ?, ?, ?, ?, ?)";
        String contientSql = "INSERT INTO Contient (IdPizza, idTaille, idCommande) VALUES (?, ?, ?)";

        try (Connection conn = dbHandler.getConnection();
             PreparedStatement commandeStmt = conn.prepareStatement(commandeSql, PreparedStatement.RETURN_GENERATED_KEYS);
             PreparedStatement contientStmt = conn.prepareStatement(contientSql)) {

            // Insertion de la commande
            commandeStmt.setString(1, commande.getAdresseCommande());
            commandeStmt.setTimestamp(2, java.sql.Timestamp.valueOf(commande.getDateCommande()));
            commandeStmt.setTimestamp(3, java.sql.Timestamp.valueOf(commande.getDateLivraison()));
            commandeStmt.setInt(4, commande.getIdClient());
            commandeStmt.setInt(5, commande.getIdLivreur());
            commandeStmt.setInt(6, commande.getIdVehicule());
            commandeStmt.executeUpdate();

            // Récupération de l'id de la commande insérée
            ResultSet generatedKeys = commandeStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int commandeId = generatedKeys.getInt(1);

                // Insertion des pizzas associées à la commande
                for (Map.Entry<Pizza,Taille> pizzas : commande.getPizzas().entrySet()) {
                    contientStmt.setInt(1, pizzas.getKey().getIdPizza());
                    contientStmt.setInt(2, pizzas.getValue().getIdTaille());
                    contientStmt.setInt(3, commandeId);

                    contientStmt.addBatch();
                }

                // Exécution de l'insertion des pizzas
                contientStmt.executeBatch();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public double getMontantTotalCommande(int idCommande) {
        String sql = "SELECT SUM(Pizza.Prix * Taille.ModificateurPrix) AS MontantTotal " +
                "FROM Contient " +
                "JOIN Pizza ON Contient.IdPizza = Pizza.IdPizza " +
                "JOIN Taille ON Contient.idTaille = Taille.idTaille " +
                "WHERE Contient.idCommande = ?";
        try (Connection conn = dbHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idCommande);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("MontantTotal");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0.0;
    }
}

