package com.example.rapizzapp.repositories;

import com.example.rapizzapp.entities.Client;
import com.example.rapizzapp.entities.Commande;
import com.example.rapizzapp.handlers.DatabaseHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientRepository {
    private DatabaseHandler dbHandler;

    private static ClientRepository clientRepository;

    private ClientRepository() {
        dbHandler = DatabaseHandler.getInstance();
    }

    public static  ClientRepository getInstance(){
        if(clientRepository == null){
            clientRepository = new ClientRepository();
        }
        return clientRepository;
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

    public boolean updateClient(Client client) {
        String sql = "UPDATE Client SET Nom = ?, Prenom = ?, NumeroAbonnement = ?, Solde = ?, Role = ? WHERE IdClient = ?";
        try (Connection conn = dbHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, client.getNom());
            pstmt.setString(2, client.getPrenom());
            pstmt.setInt(3, client.getNumeroAbonnement());
            pstmt.setInt(4, client.getSolde());
            pstmt.setString(5, client.getRole());
            pstmt.setInt(6, client.getIdClient());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public int getNombrePizzasCommandeesParClient(int clientId) {
        String sql = "SELECT COUNT(*) AS NombrePizzas " +
                "FROM Commande c " +
                "JOIN Contient co ON c.idCommande = co.idCommande " +
                "WHERE c.IdClient = ?";
        try (Connection conn = dbHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, clientId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("NombrePizzas");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public boolean deleteClient(Client client){
        String sql = "DELETE FROM Client WHERE IdClient = ?";
        try (Connection conn = dbHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, client.getIdClient());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
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
