package com.example.rapizzapp.entities;

public class Client {
    private int idClient;
    private String nom;
    private String prenom;
    private int numeroAbonnement;
    private int solde;

    // Constructeur
    public Client(int idClient, String nom, String prenom, int numeroAbonnement, int solde) {
        this.idClient = idClient;
        this.nom = nom;
        this.prenom = prenom;
        this.numeroAbonnement = numeroAbonnement;
        this.solde = solde;
    }

    // Getters et setters
    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public int getNumeroAbonnement() {
        return numeroAbonnement;
    }

    public void setNumeroAbonnement(int numeroAbonnement) {
        this.numeroAbonnement = numeroAbonnement;
    }

    public int getSolde() {
        return solde;
    }

    public void setSolde(int solde) {
        this.solde = solde;
    }
}
