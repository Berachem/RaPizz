package com.example.rapizzapp.controllers;

import com.example.rapizzapp.entities.Livreur;
import com.example.rapizzapp.repositories.LivreurRepository;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class AddLivreurController {
    public TextField nomTextField;
    public TextField prenomTextField;
    public Button saveButton;
    public Label confirmLabel;

    public LivreurRepository livreurRepository;

    public void initialize(){
        livreurRepository = LivreurRepository.getInstance();
    }

    public void addLivreur(ActionEvent actionEvent) {
        String prenom = prenomTextField.getText();
        String nom = nomTextField.getText();

        Livreur livreur = new Livreur();
        livreur.setPrenom(prenom);
        livreur.setNom(nom);

        if(livreurRepository.insertLivreur(livreur)){
            showAlert(Alert.AlertType.CONFIRMATION, "Livreur ajouté !", "Le livreur " + prenom + " " + nom + " a été ajouté !");
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.close();
        }else{
            showAlert(Alert.AlertType.ERROR, "Erreur !", "Le livreur n'a pas pu être ajouté. Veuillez réessayer");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
