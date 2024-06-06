package com.example.rapizzapp.controllers;

import com.example.rapizzapp.entities.Client;
import com.example.rapizzapp.repositories.ClientRepository;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;

public class ClientDetailsController {

    private Client client;

    private ClientRepository clientRepository;

    @FXML
    private TextField nomTextField;

    @FXML
    private TextField prenomTextField;

    @FXML
    private TextField numAboTextField;

    @FXML
    private TextField soldeTextField;

    @FXML
    private Label confirmLabel;

    @FXML
    private CheckBox adminCheckBox;

    @FXML
    private Button saveButton;

    @FXML
    private Button deleteButton;

    public void setClientDetails(Client client) {
        this.client = client;

        this.clientRepository = ClientRepository.getInstance();

        nomTextField.setText(this.client.getNom());
        prenomTextField.setText(this.client.getPrenom());
        numAboTextField.setText(String.valueOf(this.client.getNumeroAbonnement()));
        soldeTextField.setText(String.valueOf(this.client.getSolde()));
        adminCheckBox.setSelected(this.client.isAdmin());
    }

    public void checkClientDetails(ActionEvent actionEvent){
        String nom = String.valueOf(nomTextField.getText());
        String prenom = String.valueOf(prenomTextField.getText());
        String soldeStr = String.valueOf(soldeTextField.getText());
        boolean admin = adminCheckBox.isSelected();

        if (prenom == "" || nom == ""){
            showErrorAlert("Champs vides !", "Veuillez entrer un nom et un prénom valide");
        }else if(!isInteger(soldeStr)) {
            showErrorAlert("Solde incorrect !", "Veuillez entrer un solde correct et non vide");
        }else{
            int solde = Integer.parseInt(soldeStr);
            try{
                saveClientDetails(client, nom, prenom, solde, admin);
            }catch (Exception e){
            }
        }
    }

    private void saveClientDetails(Client client, String nom, String prenom, int solde, boolean admin) throws Exception{
        client.setNom(nom);
        client.setPrenom(prenom);
        client.setSolde(solde);
        client.setAdmin(admin);

        try{
            if (!clientRepository.updateClient(client)){
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

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public boolean isInteger( String input ) {
        try {
            Integer.parseInt( input );
            return true;
        }
        catch( Exception e ) {
            return false;
        }
    }

    public void deleteClient(ActionEvent actionEvent) {
        if (!clientRepository.deleteClient(this.client)){
            showErrorAlert("Erreur !", "Une erreur a eu lieu lors de la suppression de l'utilisateur !");
        }else{
            confirmLabel.setText("Utilisateur supprimé ! Veuillez fermer la fenêtre");

            nomTextField.setDisable(true);
            prenomTextField.setDisable(true);
            soldeTextField.setDisable(true);
            adminCheckBox.setDisable(true);
            saveButton.setDisable(true);
            deleteButton.setDisable(true);
        }
    }
}