package com.example.rapizzapp.controllers;

import com.example.rapizzapp.utils.ClientRepository;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;

public class CreateCommandController {

    @FXML
    public HBox form;

    private ClientRepository database;

    public void initialize() {
        database = new ClientRepository();



    }

}
