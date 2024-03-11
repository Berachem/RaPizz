package com.example.rapizzapp.utils;

import com.example.rapizzapp.entities.Client;
import javafx.fxml.FXML;

//Singleton permettant de gérer le client qui s'est connecté sur
//toute l'applis
public class UserHandler {

    private static UserHandler userHandler;
    private Client client;

    private UserHandler(){}

    public static UserHandler getInstance() {
        if(userHandler == null){
            userHandler = new UserHandler();
        }

        return userHandler;
    }

    public void setClient(Client client){
        this.client = client;
    }

    public Client getClient(){
        return this.client;
    }
}
