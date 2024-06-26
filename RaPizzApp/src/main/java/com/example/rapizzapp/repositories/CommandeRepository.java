package com.example.rapizzapp.repositories;

import com.example.rapizzapp.entities.*;
import com.example.rapizzapp.handlers.DatabaseHandler;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.time.Duration;

public class CommandeRepository {

    private DatabaseHandler dbHandler;
    private ClientRepository clientRepository;
    private LivreurRepository livreurRepository;
    private VehiculeRepository vehiculeRepository;

    private static CommandeRepository commandeRepository;

    private CommandeRepository() {
        dbHandler = DatabaseHandler.getInstance();
        clientRepository = ClientRepository.getInstance();
        livreurRepository = LivreurRepository.getInstance();
        vehiculeRepository = VehiculeRepository.getInstance();
    }

    public static CommandeRepository getInstance(){
        if(commandeRepository == null){
            commandeRepository = new CommandeRepository();
        }
        return commandeRepository;
    }

    public List<Commande> getAllCommandes() throws SQLException {
        List<Commande> commandes = new ArrayList<Commande>();

        String sql = "SELECT * FROM Commande";
        try (Connection conn = dbHandler.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Commande commande = new Commande();
                commande.setIdCommande(rs.getInt("idCommande"));
                commande.setAdresseCommande(rs.getString("adresseCommande"));
                commande.setDateCommande(rs.getTimestamp("DateCommande").toLocalDateTime());
                commande.setDateLivraison(rs.getTimestamp("DateLivraison").toLocalDateTime());

                commandes.add(commande);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        for (Commande commande: commandes){
            // Récupération des pizzas associées à la commande
            HashMap<Pizza, Taille> pizzas = getPizzasByCommandeId(commande.getIdCommande());
            commande.setPizzas(pizzas);
        }
        return commandes;
    }

    private HashMap<Pizza,Taille> getPizzasByCommandeId(int commandeId) throws SQLException {
        HashMap<Pizza,Taille> pizzas = new HashMap<>();

        String sql = "SELECT p.*,t.* FROM Pizza p INNER JOIN Contient c ON p.IdPizza = c.IdPizza NATURAL JOIN Taille t WHERE c.idCommande = ?";
        try (Connection conn = dbHandler.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

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
                taille.setModificateurPrix(rs.getString("ModificateurPrix"));
                pizza.setTaillePizza(taille.getLibelleTaille());



                pizzas.put(pizza,taille);
            }
        }

        return pizzas;
    }

    public void insertCommande(Commande commande) {
        String commandeSql = "INSERT INTO Commande (adresseCommande, DateCommande, DateLivraison, IdClient, IdLivreur, IdVehicule, gratuit) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String contientSql = "INSERT INTO Contient (IdPizza, idTaille, idCommande,gratuit) VALUES (?, ?, ?, ?)";

        try (Connection conn = dbHandler.getConnection();
             PreparedStatement commandeStmt = conn.prepareStatement(commandeSql, PreparedStatement.RETURN_GENERATED_KEYS);
             PreparedStatement contientStmt = conn.prepareStatement(contientSql)) {

            // Insertion de la commande
            commandeStmt.setString(1, commande.getAdresseCommande());
            commandeStmt.setTimestamp(2, java.sql.Timestamp.valueOf(commande.getDateCommande()));
            commandeStmt.setTimestamp(3, java.sql.Timestamp.valueOf(commande.getDateLivraison()));
            commandeStmt.setInt(4, commande.getClient().getIdClient());
            commandeStmt.setInt(5, commande.getLivreur().getIdLivreur());
            commandeStmt.setInt(6, commande.getVehicule().getIdVehicule());
            commandeStmt.setBoolean(7, commande.isGratuit());
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
                    contientStmt.setBoolean(4, pizzas.getKey().isGratuit());

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
        String sql = "SELECT SUM(ROUND(Pizza.Prix * (Taille.ModificateurPrix+1))) AS MontantTotal " +
                "FROM Contient " +
                "JOIN Pizza ON Contient.IdPizza = Pizza.IdPizza " +
                "JOIN Taille ON Contient.idTaille = Taille.idTaille " +
                "JOIN Commande ON Contient.idCommande = Commande.idCommande " +
                "WHERE Contient.idCommande = ? " +
                "AND Contient.gratuit IS NOT TRUE " +
                "AND Commande.gratuit IS NOT TRUE ";
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

    public double getPrixMoyenCommandes(){
        String sql = "SELECT AVG(P.Prix * COALESCE(CAST(T.ModificateurPrix AS DECIMAL(5,2)), 1)) AS PrixMoyenCommande " +
                "FROM Commande Co " +
                "JOIN Contient Ct ON Co.idCommande = Ct.idCommande " +
                "JOIN Pizza P ON Ct.IdPizza = P.IdPizza " +
                "JOIN Taille T ON Ct.idTaille = T.idTaille";
        try (Connection conn = dbHandler.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble("PrixMoyenCommande");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0.0;
    }

    public Commande getLastCommandForLivreur(int livreurId) {
        Commande commande = null;

        // Requête pour récupérer la commande associée à la plus grande date de livraison pour un livreur spécifique
        String sql = "SELECT * FROM Commande " +
                "WHERE IdLivreur = ? " +
                "AND DateLivraison = (SELECT MAX(DateLivraison) FROM Commande WHERE IdLivreur = ?)";

        try (Connection conn = dbHandler.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, livreurId);
            stmt.setInt(2, livreurId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int idCommande = rs.getInt("idCommande");
                    String adresseCommande = rs.getString("adresseCommande");
                    LocalDateTime dateCommande = rs.getTimestamp("DateCommande").toLocalDateTime();
                    LocalDateTime dateLivraison = rs.getTimestamp("DateLivraison").toLocalDateTime();

                    int idClient= rs.getInt("IdClient");
                    int idVehicule = rs.getInt("IdVehicule");
                    boolean gratuit = rs.getBoolean("gratuit");

                    Client client = clientRepository.getClient(idClient);
                    Vehicule vehicule = vehiculeRepository.getVehicule(idVehicule);
                    Livreur livreur = livreurRepository.getLivreur(livreurId);
                    // Vous pouvez ajouter d'autres colonnes nécessaires ici
                    // Créer un HashMap pour stocker les pizzas commandées
                    HashMap<Pizza, Taille> pizzas = getPizzasByCommandeId(idCommande);
                    // Créer un objet Commande avec les données récupérées
                    commande = new Commande(idCommande, adresseCommande, dateCommande, dateLivraison, client, livreur, vehicule, pizzas,gratuit);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return commande;
    }

    public Timestamp getLastOrderDateTime() {
        String sql = "SELECT MAX(DateCommande) AS DerniereCommande FROM Commande";

        try (Connection conn = dbHandler.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getTimestamp("DerniereCommande");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Time getOrderAverageTime() {
        String sql = "SELECT AVG(TIMESTAMPDIFF(SECOND, DateCommande, DateLivraison)) AS TempsLivraisonMoyen " +
        "FROM Commande";

        try (Connection conn = dbHandler.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                int tempsLivraisonMoyen = rs.getInt("TempsLivraisonMoyen");
                int hours = tempsLivraisonMoyen / 3600;
                int minutes = tempsLivraisonMoyen / 60;
                int secondes = minutes % 60;
                Time time = new Time(hours, minutes, secondes);
                return time;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new Time(0);
    }

    public int getcommandAmount() {
        String sql = "SELECT count(*) AS nombreCommandes FROM Commande";
        try (Connection conn = dbHandler.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("nombreCommandes");
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        }
        return -1;
    }


    public List<Commande> getClientOrderHistory(int clientId) throws SQLException {
        List<Commande> commandes = new ArrayList<>();
        String sql = "SELECT * FROM Commande WHERE IdClient = ?";

        try (Connection conn = dbHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, clientId);
            ResultSet rs = pstmt.executeQuery();

            ArrayList<Integer> clientIds = new ArrayList<>();
            ArrayList<Integer> livreursIds = new ArrayList<>();
            ArrayList<Integer> vehiculeIds = new ArrayList<>();
            while (rs.next()) {
                Commande commande = new Commande();
                commande.setIdCommande(rs.getInt("idCommande"));
                commande.setAdresseCommande(rs.getString("adresseCommande"));
                commande.setDateCommande(rs.getTimestamp("DateCommande").toLocalDateTime());
                commande.setDateLivraison(rs.getTimestamp("DateLivraison") != null ? rs.getTimestamp("DateLivraison").toLocalDateTime() : null);
                commande.setGratuit(rs.getBoolean("gratuit"));
                //vu que chaque statement dans TOUT LE CODE reset le result set
                //il faut stocker avant de refaire une requette
                clientIds.add(rs.getInt("IdClient"));
                livreursIds.add(rs.getInt("IdLivreur"));
                vehiculeIds.add(rs.getInt("IdVehicule"));

                commandes.add(commande);
            }

            //on récupères les objets des infos stockés
            for(int i=0;i<commandes.size();i++) {
                Commande commande = commandes.get(i);
                commande.setClient(clientRepository.getClient(clientIds.get(i)));
                commande.setLivreur(livreurRepository.getLivreur(livreursIds.get(i)));
                commande.setVehicule(vehiculeRepository.getVehicule(vehiculeIds.get(i)));
            }


        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        // Calculer le montant total pour chaque commande
        for (Commande commande : commandes) {
            commande.setMontant(commandeRepository.getMontantTotalCommande(commande.getIdCommande()));

            // on retrouve les pizzas de la commande dans la table Contient
            HashMap<Pizza, Taille> pizzas = getPizzasByCommandeId(commande.getIdCommande());
            commande.setPizzas(pizzas);
        }



        return commandes;
    }

    public Commande getLastCommandeFromClient(int clientId) {
        Commande commande = null;

        String sql = "SELECT * FROM Commande " +
                "WHERE IdClient = ? " +
                "ORDER BY DateCommande DESC " +
                "LIMIT 1";

        try (Connection conn = dbHandler.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clientId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int idCommande = rs.getInt("idCommande");
                    String adresseCommande = rs.getString("adresseCommande");
                    LocalDateTime dateCommande = rs.getTimestamp("DateCommande").toLocalDateTime();
                    LocalDateTime dateLivraison = rs.getTimestamp("DateLivraison") != null ? rs.getTimestamp("DateLivraison").toLocalDateTime() : null;

                    int idLivreur = rs.getInt("IdLivreur");
                    int idVehicule = rs.getInt("IdVehicule");
                    boolean gratuit = rs.getBoolean("gratuit");

                    Client client = clientRepository.getClient(clientId);
                    System.out.println(clientId + " : "+client);
                    Livreur livreur = livreurRepository.getLivreur(idLivreur);
                    Vehicule vehicule = vehiculeRepository.getVehicule(idVehicule);

                    // Récupération des pizzas associées à la commande
                    HashMap<Pizza, Taille> pizzas = getPizzasByCommandeId(idCommande);

                    // Créer un objet Commande avec les données récupérées
                    commande = new Commande(idCommande, adresseCommande, dateCommande, dateLivraison, client, livreur, vehicule, pizzas,gratuit);

                    // Calculer et définir le montant total de la commande
                    commande.setMontant(getMontantTotalCommande(idCommande));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return commande;
    }
    
    public String getBestWeekDay() {
        String sql = "SELECT DAYNAME(DateCommande) AS JourSemaine, SUM(P.Prix) AS TotalVentes " +
                "FROM Commande C " +
                "JOIN Contient Ct ON C.idCommande = Ct.idCommande " +
                "JOIN Pizza P ON Ct.IdPizza = P.IdPizza " +
                "GROUP BY DAYNAME(DateCommande) " +
                "ORDER BY TotalVentes DESC " +
                "LIMIT 1";
        try (Connection conn = dbHandler.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getString("JourSemaine");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "not found";
    }

    public void updateCommande(Commande commande){
        String commandeSql = "UPDATE Commande SET adresseCommande = ?, DateCommande = ?, DateLivraison = ?, IdClient = ?, IdLivreur = ?, IdVehicule = ?, gratuit = ? WHERE IdCommande = ?";
        try (Connection conn = dbHandler.getConnection();
             PreparedStatement commandeStmt = conn.prepareStatement(commandeSql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            // Insertion de la commande
            commandeStmt.setString(1, commande.getAdresseCommande());
            commandeStmt.setTimestamp(2, java.sql.Timestamp.valueOf(commande.getDateCommande()));
            commandeStmt.setTimestamp(3, java.sql.Timestamp.valueOf(commande.getDateLivraison()));
            commandeStmt.setInt(4, commande.getClient().getIdClient());
            commandeStmt.setInt(5, commande.getLivreur().getIdLivreur());
            commandeStmt.setInt(6, commande.getVehicule().getIdVehicule());
            commandeStmt.setBoolean(7, commande.isGratuit());
            commandeStmt.setInt(8, commande.getIdCommande());
            commandeStmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public boolean deleteCommande(Commande commande){
        String sql = "DELETE FROM Commande WHERE IdCommande = ?";
        try (Connection conn = dbHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, commande.getIdCommande());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
}

