// /!\ PLEASE READ CAREFULLY /!\ //
// THIS IS A DATABASE HANDLER EXAMPLE //
// YOU SHOULD CREATE YOUR OWN CLASS DatabaseHandler.java //
// AND FILLING YOUR OWN DATABASE CREDENTIALS //
// BEFORE USING THE APPLICATION //

package com.example.rapizzapp.handlers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHandlerExample {

    private String url = "jdbc:mysql://localhost:3306/rapizz";
    private String user = "root";
    private String password = "password";
    private Connection conn;

    private static DatabaseHandlerExample databaseHandlerExample;

    private DatabaseHandlerExample() {
        establishConnection();
    }

    public static DatabaseHandlerExample getInstance(){
        if(databaseHandlerExample == null){
            databaseHandlerExample = new DatabaseHandlerExample();
        }
        return databaseHandlerExample;
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
