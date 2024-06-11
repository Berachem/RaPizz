package com.example.rapizzapp.repositories;

import com.example.rapizzapp.entities.*;
import com.example.rapizzapp.handlers.DatabaseHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PizzaRepository {

    private DatabaseHandler dbHandler;

    private static PizzaRepository pizzaRepository;

    private PizzaRepository() {
        dbHandler = DatabaseHandler.getInstance();
    }

    public static PizzaRepository getInstance(){
        if(pizzaRepository == null){
            pizzaRepository = new PizzaRepository();
        }
        return pizzaRepository;
    }

    public List<Pizza> getAllPizza() {
        List<Pizza> pizzas= new ArrayList<Pizza>();
        Ingredient ingredient = null;

        String sql = "SELECT * FROM Pizza";
        try (Connection conn = dbHandler.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                pizzas.add(new Pizza(rs.getInt("IdPizza"), rs.getString("libellePizza"), rs.getDouble("Prix"), "", rs.getString("Image"),new ArrayList<>()));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        // on récupère les ingrédients de chaque pizza (table Contient, jointure avec la table Ingrédient)
        for (Pizza pizza : pizzas) {
            List<Ingredient> ingredients = new ArrayList<>();
            String sqlIngredients = "SELECT * FROM Compose, Ingrédient WHERE IdPizza = " + pizza.getIdPizza() + " AND Compose.IdIngredient = Ingrédient.IdIngredient";
            try (Connection conn = dbHandler.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sqlIngredients)) {
                while (rs.next()) {
                    ingredient = new Ingredient(rs.getInt("IdIngredient"), rs.getString("libelleIngredient"));
                    ingredients.add(ingredient);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            pizza.setIngredients(ingredients);
        }


        return pizzas;
    }

    public Pizza getPizza(int idPizza) {
        Pizza pizza = null;
        Ingredient ingredient = null;

        String sql = "SELECT * FROM Pizza WHERE idPizza  = ?";
        try (Connection conn = dbHandler.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPizza);
            try (ResultSet rs = stmt.executeQuery()){
                if (rs.next()) {
                    pizza = new Pizza(
                            rs.getInt("IdPizza"),
                            rs.getString("libellePizza"),
                            rs.getDouble("Prix"),
                            "",
                            rs.getString("Image"),
                            new ArrayList<>()
                    );
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        // on récupère les ingrédients de la pizza (table Contient, jointure avec la table Ingrédient)
        List<Ingredient> ingredients = new ArrayList<>();
        String sqlIngredients = "SELECT * FROM Compose, Ingrédient WHERE IdPizza = " + pizza.getIdPizza() + " AND Compose.IdIngredient = Ingrédient.IdIngredient";
        try (Connection conn = dbHandler.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlIngredients)) {
            while (rs.next()) {
                ingredient = new Ingredient(rs.getInt("IdIngredient"), rs.getString("libelleIngredient"));
                ingredients.add(ingredient);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        pizza.setIngredients(ingredients);

        return pizza;
    }

    public String getBestPizza(){
        String sql = "SELECT P.LibellePizza, COUNT(Ct.IdCommande) AS NombreCommandes " +
                "FROM Pizza P " +
                "JOIN Contient Ct ON P.IdPizza = Ct.IdPizza " +
                "GROUP BY P.LibellePizza " +
                "ORDER BY NombreCommandes DESC " +
                "LIMIT 1";
        try (Connection conn = dbHandler.getConnection();
            Statement stmt = conn.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(sql)){
                if (rs.next()) {
                    return rs.getString("LibellePizza");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public String getWorstPizza(){
        String sql = "SELECT P.LibellePizza, COUNT(Ct.IdCommande) AS NombreCommandes " +
                "FROM Pizza P " +
                "JOIN Contient Ct ON P.IdPizza = Ct.IdPizza " +
                "GROUP BY P.LibellePizza " +
                "ORDER BY NombreCommandes ASC " +
                "LIMIT 1";
        try (Connection conn = dbHandler.getConnection();
             Statement stmt = conn.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(sql)){
                if (rs.next()) {
                    return rs.getString("LibellePizza");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public String getMostOrderedIngredient() {
        String sql = "SELECT I.libelleIngredient, COUNT(Cp.IdPizza) AS NombreCommandes " +
                "FROM Ingrédient I " +
                "JOIN Compose Cp ON I.IdIngredient = Cp.IdIngredient " +
                "JOIN Contient C ON Cp.IdPizza = C.IdPizza " +
                "GROUP BY I.libelleIngredient " +
                "ORDER BY NombreCommandes DESC " +
                "LIMIT 1";

        try (Connection conn = dbHandler.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getString("libelleIngredient");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }


    public static List<Pizza> deepCopyPizzaList(List<Pizza> originalList) {
        List<Pizza> copiedList = new ArrayList<>();

        for (Pizza pizza : originalList) {
            // Copie profonde des ingrédients
            List<Ingredient> copiedIngredients = new ArrayList<>(pizza.getIngredients());

            // Création d'un nouvel objet Pizza avec les valeurs copiées
            Pizza copiedPizza = new Pizza(
                    pizza.getIdPizza(),
                    pizza.getLibellePizza(),
                    pizza.getPrix(),
                    pizza.getTaillePizza(),
                    pizza.getImagePizza(),
                    copiedIngredients,
                    pizza.isGratuit()
            );

            copiedList.add(copiedPizza);
        }

        return copiedList;
    }

    public void insertPizza(Pizza pizza) {
        String pizzaSql = "INSERT INTO Pizza(LibellePizza, Prix, Image) VALUES (?, ?, ?)";
        String composeSql = "INSERT INTO Compose(IdIngredient, IdPizza) VALUES (?, ?)";

        try (Connection conn = dbHandler.getConnection();
             PreparedStatement pizzaStmt = conn.prepareStatement(pizzaSql, PreparedStatement.RETURN_GENERATED_KEYS);
             PreparedStatement composeStmt = conn.prepareStatement(composeSql)) {

            // Insertion de la pizza
            pizzaStmt.setString(1, pizza.getLibellePizza());
            pizzaStmt.setDouble(2, pizza.getPrix());
            pizzaStmt.setString(3, pizza.getImagePizza());
            pizzaStmt.executeUpdate();

            // Récupération de l'id de la pizza insérée
            ResultSet generatedKeys = pizzaStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int pizzaId = generatedKeys.getInt(1);

                // Insertion des ingrédients associées à la pizza
                for (Ingredient ingredient : pizza.getIngredients()) {
                    composeStmt.setInt(1, ingredient.getIdIngredient());
                    composeStmt.setInt(2, pizzaId);

                    composeStmt.addBatch();
                }

                // Exécution de l'insertion des pizzas
                composeStmt.executeBatch();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void updatePizza(Pizza pizza) {
        String pizzaSql = "UPDATE Pizza SET LibellePizza = ?, Prix = ?, Image = ? WHERE IdPizza = ?";

        try (Connection conn = dbHandler.getConnection();
             PreparedStatement pizzaStmt = conn.prepareStatement(pizzaSql);){

            // modification de la pizza
            pizzaStmt.setString(1, pizza.getLibellePizza());
            pizzaStmt.setDouble(2, pizza.getPrix());
            pizzaStmt.setString(3, pizza.getImagePizza());
            pizzaStmt.setInt(4, pizza.getIdPizza());
            pizzaStmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public boolean deletePizza(Pizza pizza){
        String sql = "DELETE FROM Pizza WHERE IdPizza = ?";
        try (Connection conn = dbHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, pizza.getIdPizza());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

}
