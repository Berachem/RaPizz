package com.example.rapizzapp.entities;

public class Livreur {
    private int idLivreur;
    private String nom;
    private String prenom;

    public Livreur() {
        this.idLivreur = -1;
        this.nom = "";
        this.prenom = "";
    }

    public Livreur(int idLivreur, String nom, String prenom) {
        this.idLivreur = idLivreur;
        this.nom = nom;
        this.prenom = prenom;
    }

    public int getIdLivreur() {
        return idLivreur;
    }

    public void setIdLivreur(int idLivreur) {
        this.idLivreur = idLivreur;
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
}
