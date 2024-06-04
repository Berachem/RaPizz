package com.example.rapizzapp.controllers;

import com.example.rapizzapp.entities.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ClientDetailsController {

    private Client client;

    @FXML
    private TextField nomTextField;

    @FXML
    private TextField prenomTextField;

    @FXML
    private TextField numAboTextField;

    @FXML
    private TextField soldeTextField;

    public void setClientDetails(Client client) {
        this.client = client;

        nomTextField.setText(this.client.getNom());
        prenomTextField.setText(this.client.getPrenom());
        numAboTextField.setText(String.valueOf(this.client.getNumeroAbonnement()));
        soldeTextField.setText(String.valueOf(this.client.getSolde()));
    }

    public void saveClientDetails(ActionEvent actionEvent) {
    }
}
