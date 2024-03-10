package com.example.rapizzapp.login;

import com.example.rapizzapp.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

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
    public void createAccountAction(ActionEvent event) {
        Parent root;
        try {
            root = FXMLLoader.load(HelloApplication.class.getResource("createAccount.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Créer un compte");
            stage.setScene(new Scene(root, 450, 450));
            stage.show();
            // Hide this current window (if this is what you want)
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        // Ajoutez ici la logique pour créer un compte
    }
}
