package com.example.rapizzapp.repositories;

import com.example.rapizzapp.entities.TypeVehicule;
import com.example.rapizzapp.entities.Vehicule;
import com.example.rapizzapp.handlers.DatabaseHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VehiculeRepository {

    private DatabaseHandler dbHandler;
    private TypeVehiculeRepository typeVehiculeRepository;

    private static VehiculeRepository vehiculeRepository;

    private VehiculeRepository() {
        dbHandler = DatabaseHandler.getInstance();
        typeVehiculeRepository = TypeVehiculeRepository.getInstance();
    }

    public static VehiculeRepository getInstance(){
        if(vehiculeRepository == null){
            vehiculeRepository = new VehiculeRepository();
        }
        return  vehiculeRepository;
    }

    public List<Vehicule> getAll() {
        List<Vehicule> vehicules = new ArrayList<>();

        String sql = "SELECT * FROM Vehicule";
        try (Connection conn = dbHandler.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int idVehicule = rs.getInt("IdVehicule");
                String nom = rs.getString("Nom");
                int typeId = rs.getInt("IdType");
                TypeVehicule typeVehicule = typeVehiculeRepository.getTypeVehiculeById(typeId);
                Vehicule vehicule = new Vehicule(idVehicule, typeVehicule, nom);
                vehicules.add(vehicule);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return vehicules;
    }

    public Vehicule getRandomVehicule() {
        Vehicule vehicule = null;

        String sql = "SELECT * FROM Vehicule ORDER BY RAND() LIMIT 1";
        try (Connection conn = dbHandler.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                int idVehicule = rs.getInt("IdVehicule");
                String nom = rs.getString("Nom");
                TypeVehicule typeVehicule = typeVehiculeRepository.getTypeVehiculeById(rs.getInt("IdType"));
                vehicule = new Vehicule(idVehicule, typeVehicule, nom);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return vehicule;
    }

    public Vehicule getRandomAvailableVehicule() {
        Vehicule vehicule = null;

        // Requête pour récupérer les véhicules disponibles (non associés à des commandes en cours de livraison)
        String sql = "SELECT * FROM Vehicule " +
                "WHERE IdVehicule NOT IN " +
                "(SELECT IdVehicule FROM Commande WHERE DateLivraison > NOW()) " +
                "ORDER BY RAND() LIMIT 1";

        try (Connection conn = dbHandler.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                int idVehicule = rs.getInt("IdVehicule");
                // Vous devez récupérer les informations supplémentaires du véhicule ici
                String nom = rs.getString("Nom");
                TypeVehicule type = typeVehiculeRepository.getTypeVehiculeById(rs.getInt("IdType"));
                // Instancier l'objet Vehicule avec les informations récupérées
                vehicule = new Vehicule(idVehicule, type, nom);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return vehicule;
    }

    public Vehicule getVehicule(int idVehicule) {
        Vehicule vehicule = null;

        String sql = "SELECT * FROM Vehicule WHERE IdVehicule = ?";
        try (Connection conn = dbHandler.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idVehicule);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String nom = rs.getString("Nom");
                    int typeId = rs.getInt("IdType");
                    TypeVehicule typeVehicule = typeVehiculeRepository.getTypeVehiculeById(typeId);
                    vehicule = new Vehicule(idVehicule, typeVehicule, nom);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return vehicule;
    }

    public boolean insertVehicule(Vehicule vehicule) {
        String sql = "INSERT INTO Vehicule(IdType, Nom) VALUES (?, ?)";
        try (Connection conn = dbHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, vehicule.getType().getIdType());
            pstmt.setString(2, vehicule.getNom());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public boolean updateVehicule(Vehicule vehicule) {
        String sql = "UPDATE Vehicule SET IdType = ?, Nom = ? WHERE IdVehicule = ?";
        try (Connection conn = dbHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, vehicule.getType().getIdType());
            pstmt.setString(2, vehicule.getNom());
            pstmt.setInt(3, vehicule.getIdVehicule());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public boolean deleteVehicule(Vehicule vehicule) {
        String sql = "DELETE FROM TypeVehicule WHERE IdType = ?";
        try (Connection conn = dbHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, vehicule.getIdVehicule());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
}
