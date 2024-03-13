package com.example.rapizzapp.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHandler {

    // Berachem :
    private String url = "jdbc:mysql://localhost:3306/rapizz";
    private String user = "root";
    private String password = "";

    private Connection conn;

    public DatabaseHandler() {
        establishConnection();
    }

    private void establishConnection() {
        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Vous pourriez envisager de lancer une exception personnalisée ici pour notifier l'échec de la connexion
        }
    }

    public Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                establishConnection();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return conn;
    }

    public void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                // Gérer les exceptions ici
            }
        }
    }
}
