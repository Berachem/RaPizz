package com.example.rapizzapp.handlers;

import com.example.rapizzapp.entities.Client;

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

    public static void resetLogin() {
        userHandler = null;
    }

    public void setClient(Client client){
        this.client = client;
    }

    public Client getClient(){
        return this.client;
    }
}
