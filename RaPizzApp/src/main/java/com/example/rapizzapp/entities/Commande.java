package com.example.rapizzapp.entities;

import java.time.LocalDateTime;
import java.util.HashMap;

public class Commande {
    private int idCommande;
    private String adresseCommande;
    private LocalDateTime dateCommande;
    private LocalDateTime dateLivraison;
    private int idClient;
    private int idLivreur;
    private int idVehicule;

    private HashMap<Pizza,Taille> pizzas;

    private double montant = 0.0; // Valeur par défaut

    // Constructeur
    public Commande() {
        this.idCommande = 0;
        this.adresseCommande = "";
        this.dateCommande = LocalDateTime.now();
        this.dateLivraison = LocalDateTime.now();
        this.idClient = 0;
        this.idLivreur = 0;
        this.idVehicule = 0;
        this.pizzas =new HashMap<Pizza,Taille>();
    }

    public Commande(int idCommande, String adresseCommande, LocalDateTime dateCommande, LocalDateTime dateLivraison, int idClient, int idLivreur, int idVehicule,HashMap<Pizza,Taille> commande) {
        this.idCommande = idCommande;
        this.adresseCommande = adresseCommande;
        this.dateCommande = dateCommande;
        this.dateLivraison = dateLivraison;
        this.idClient = idClient;
        this.idLivreur = idLivreur;
        this.idVehicule = idVehicule;
        this.pizzas = commande;
    }

    // Getters et setters
    public int getIdCommande() {
        return idCommande;
    }

    public void setIdCommande(int idCommande) {
        this.idCommande = idCommande;
    }

    public String getAdresseCommande() {
        return adresseCommande;
    }

    public void setAdresseCommande(String adresseCommande) {
        this.adresseCommande = adresseCommande;
    }

    public LocalDateTime getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(LocalDateTime dateCommande) {
        this.dateCommande = dateCommande;
    }

    public LocalDateTime getDateLivraison() {
        return dateLivraison;
    }

    public void setDateLivraison(LocalDateTime dateLivraison) {
        this.dateLivraison = dateLivraison;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public int getIdLivreur() {
        return idLivreur;
    }

    public void setIdLivreur(int idLivreur) {
        this.idLivreur = idLivreur;
    }

    public int getIdVehicule() {
        return idVehicule;
    }

    public void setIdVehicule(int idVehicule) {
        this.idVehicule = idVehicule;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public HashMap<Pizza,Taille> getPizzas(){ return this.pizzas; }

    public void setPizzas(HashMap<Pizza,Taille> pizzas){ this.pizzas = pizzas; }
}
