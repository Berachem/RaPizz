package com.example.rapizzapp.entities;

public class Vehicule {
    private int idVehicule;
    private TypeVehicule type;
    private String nom;

    public Vehicule() {
        this.idVehicule = -1;
        this.type = new TypeVehicule();
        this.nom = "";
    }

    public Vehicule(int idVehicule, TypeVehicule type, String nom) {
        this.idVehicule = idVehicule;
        this.type = type;
        this.nom = nom;
    }

    public int getIdVehicule() {
        return idVehicule;
    }

    public void setIdVehicule(int idVehicule) {
        this.idVehicule = idVehicule;
    }

    public TypeVehicule getType() {
        return type;
    }

    public void setType(TypeVehicule type) {
        this.type = type;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString(){
        return nom;
    }
}
