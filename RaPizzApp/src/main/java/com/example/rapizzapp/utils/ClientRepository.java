package com.example.rapizzapp.utils;

import com.example.rapizzapp.entities.Client;
import com.example.rapizzapp.entities.Commande;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientRepository {
    private DatabaseHandler dbHandler;
    private CommandeRepository commandeRepository;
    public ClientRepository() {
        dbHandler = new DatabaseHandler();
        commandeRepository = new CommandeRepository();
    }

    public List<Client> getAllClients() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM Client";
        try (Connection conn = dbHandler.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                clients.add(new Client(rs.getInt("IdClient"), rs.getString("Nom"), rs.getString("Prenom"), rs.getInt("NumeroAbonnement"), rs.getInt("Solde"), rs.getString("Role")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return clients;
    }

    public Client getClient(int clientId) {
        String sql = "SELECT * FROM Client WHERE IdClient = ?";
        try (Connection conn = dbHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, clientId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Client(rs.getInt("IdClient"), rs.getString("Nom"), rs.getString("Prenom"), rs.getInt("NumeroAbonnement"), rs.getInt("Solde"), rs.getString("Role"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Client getClientByNumeroAbonnement(int numeroAbonnement) {
        String sql = "SELECT * FROM Client WHERE NumeroAbonnement = ?";
        try (Connection conn = dbHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, numeroAbonnement);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Client(rs.getInt("IdClient"), rs.getString("Nom"), rs.getString("Prenom"), rs.getInt("NumeroAbonnement"), rs.getInt("Solde"), rs.getString("Role"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public boolean insertClient(Client client) {
        String sql = "INSERT INTO Client (Nom, Prenom, NumeroAbonnement, Solde) VALUES (?, ?, ?, ?)";
        try (Connection conn = dbHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, client.getNom());
            pstmt.setString(2, client.getPrenom());
            pstmt.setInt(3, client.getNumeroAbonnement());
            pstmt.setInt(4, client.getSolde());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public List<Commande> getClientOrderHistory(int clientId) {
        List<Commande> commandes = new ArrayList<>();
        String sql = "SELECT * FROM Commande WHERE IdClient = ?";

        try (Connection conn = dbHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, clientId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Commande commande = new Commande();
                commande.setIdCommande(rs.getInt("idCommande"));
                commande.setAdresseCommande(rs.getString("adresseCommande"));
                commande.setDateCommande(rs.getTimestamp("DateCommande").toLocalDateTime());
                commande.setDateLivraison(rs.getTimestamp("DateLivraison") != null ? rs.getTimestamp("DateLivraison").toLocalDateTime() : null);
                commande.setIdClient(rs.getInt("IdClient"));
                commande.setIdLivreur(rs.getInt("IdLivreur"));
                commande.setIdVehicule(rs.getInt("IdVehicule"));
                commandes.add(commande);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        // Calculer le montant total pour chaque commande
        for (Commande commande : commandes) {
            commande.setMontant(commandeRepository.getMontantTotalCommande(commande.getIdCommande()));
        }

        return commandes;
    }

/*
    public Commande getCurrentOrder(int clientId) {
        // Implémentation pour obtenir la commande en cours du client
    }

    public void createNewOrder(Commande newOrder) {
        // Implémentation pour créer une nouvelle commande
        // Cela devrait être implémenté dans le futur
    }*/

    // Vous pouvez ajouter d'autres méthodes de service nécessaires ici.
}
