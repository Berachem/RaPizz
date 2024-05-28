package com.example.rapizzapp.repositories;

import com.example.rapizzapp.entities.Taille;
import com.example.rapizzapp.entities.TypeVehicule;
import com.example.rapizzapp.handlers.DatabaseHandler;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TypeVehiculeRepository {

    private DatabaseHandler dbHandler;

    private static TypeVehiculeRepository typeVehiculeRepository;

    private TypeVehiculeRepository() {
        dbHandler = DatabaseHandler.getInstance();
    }

    public static TypeVehiculeRepository getInstance(){
        if(typeVehiculeRepository == null){
            typeVehiculeRepository = new TypeVehiculeRepository();
        }
        return typeVehiculeRepository;
    }

    public List<TypeVehicule> getAll() {
        List<TypeVehicule> types = new ArrayList<>();

        String sql = "SELECT * FROM TypeVehicule";
        try (Connection conn = dbHandler.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int idType = rs.getInt("IdType");
                String libelleVehicule = rs.getString("LibelleVehicule");
                TypeVehicule typeVehicule = new TypeVehicule(idType, libelleVehicule);
                types.add(typeVehicule);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return types;
    }

    public TypeVehicule getTypeVehiculeById(int id) {
        TypeVehicule typeVehicule = null;

        String sql = "SELECT * FROM TypeVehicule WHERE IdType = ?";
        try (Connection conn = dbHandler.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int typeId = rs.getInt("IdType");
                    String libelleVehicule = rs.getString("LibelleVehicule");
                    typeVehicule = new TypeVehicule(typeId, libelleVehicule);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return typeVehicule;
    }

    public boolean insertTypeVehicule(TypeVehicule typeVehicule) {
        String sql = "INSERT INTO TypeVehicule(LibelleVehicule) VALUES (?)";
        try (Connection conn = dbHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, typeVehicule.getLibelleVehicule());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public boolean updateTypeVehicule(TypeVehicule typeVehicule) {
        String sql = "UPDATE TypeVehicule SET LibelleVehicule = ? WHERE IdType = ?";
        try (Connection conn = dbHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, typeVehicule.getLibelleVehicule());
            pstmt.setInt(2, typeVehicule.getIdType());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public boolean deleteTaille(TypeVehicule typeVehicule) {
        String sql = "DELETE FROM TypeVehicule WHERE IdType = ?";
        try (Connection conn = dbHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, typeVehicule.getIdType());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
}
