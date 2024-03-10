package com.example.rapizzapp.login;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML
    public TextField usernameField;

    @FXML
    public TextField passwordField;

    @FXML
    public void loginAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Ajoutez ici la logique de connexion
    }

    @FXML
    public void createAccountAction() {
        System.out.println("hello");
        // Ajoutez ici la logique pour créer un compte
    }
}
