package com.example.rapizzapp.controllers;

import com.example.rapizzapp.entities.Client;
import com.example.rapizzapp.entities.Livreur;
import com.example.rapizzapp.repositories.LivreurRepository;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;

public class LivreurDetailsController {

    private Livreur livreur;
    private LivreurRepository livreurRepository;
    public TextField nomTextField;
    public TextField prenomTextField;
    public Button saveButton;
    public Label confirmLabel;
    public Button deleteButton;

    public void setLivreurDetails(Livreur livreur) {
        this.livreur = livreur;

        this.livreurRepository = LivreurRepository.getInstance();

        nomTextField.setText(this.livreur.getNom());
        prenomTextField.setText(this.livreur.getPrenom());

    }

    public void checkLivreurDetails(ActionEvent actionEvent) {
        String nom = String.valueOf(nomTextField.getText());
        String prenom = String.valueOf(prenomTextField.getText());

        if (prenom == "" || nom == ""){
            showErrorAlert("Champs vides !", "Veuillez entrer un nom et un prénom valide");
        }else{
            try{
                saveLivreurDetails(livreur, nom, prenom);
            }catch (Exception e){
            }
        }
    }

    private void saveLivreurDetails(Livreur livreur, String nom, String prenom) throws Exception{
        livreur.setNom(nom);
        livreur.setPrenom(prenom);

        try{
            if (!livreurRepository.updateLivreur(livreur)){
                showErrorAlert("Erreur !", "Aucun changement n'a eu lieu, veuillez vérifier les valeurs ou réessayer plus tard");
            }else{
                confirmLabel.setText("Changements enregistrés !");
            }
        }catch (Exception e){
            showErrorAlert("Erreur !", "La base de données a échoué l'enregistrement des données, veuillez vérifier les valeurs ou réessayer plus tard");
        }

        PauseTransition delay = new PauseTransition(Duration.seconds(1.5));
        delay.setOnFinished(event -> confirmLabel.setText(""));
        delay.play();
    }

    public void deleteLivreur(ActionEvent actionEvent) {
        if (!livreurRepository.deleteLiveur(this.livreur)){
            showErrorAlert("Erreur !", "Une erreur a eu lieu lors de la suppression du livreur !");
        }else{
            confirmLabel.setText("Livreur supprimé ! Veuillez fermer la fenêtre");

            nomTextField.setDisable(true);
            prenomTextField.setDisable(true);
            saveButton.setDisable(true);
            deleteButton.setDisable(true);
        }
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
