package com.example.rapizzapp.login;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    protected void loginAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Ajoutez ici la logique de connexion
    }

    @FXML
    protected void createAccountAction() {
        // Ajoutez ici la logique pour créer un compte
    }
}
