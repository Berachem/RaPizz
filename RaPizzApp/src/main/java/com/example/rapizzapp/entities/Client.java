package com.example.rapizzapp.entities;

public class Client {
    private int idClient;
    private String nom;
    private String prenom;
    private int numeroAbonnement;
    private int solde;

    private String role;

    public Client() {
        this.idClient = -1;
        this.nom = "";
        this.prenom = "";
        this.numeroAbonnement = -1;
        this.solde = -1;
        this.role = "";
    }

    // Constructeur
    public Client(int idClient, String nom, String prenom, int numeroAbonnement, int solde, String role ) {
        this.idClient = idClient;
        this.nom = nom;
        this.prenom = prenom;
        this.numeroAbonnement = numeroAbonnement;
        this.solde = solde;
        this.role = role;
    }
    public Client(String nom, String prenom, int numeroAbonnement, int solde) {
        this(0, nom, prenom, numeroAbonnement, solde, "USER");
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isAdmin() {
        return role.equalsIgnoreCase("ADMIN");
    }

    @Override
    public String toString(){
        return nom + " " + prenom;
    }

    public void addSolde(double amount) {
        solde += amount;
    }
}
