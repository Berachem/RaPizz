package com.example.rapizzapp.entities;

public class TypeVehicule {
    private int idType;
    private String libelleVehicule;

    public TypeVehicule() {
       this.idType=-1;
       this.libelleVehicule ="";
    }

    public TypeVehicule(int idType, String libelleVehicule) {
        this.idType = idType;
        this.libelleVehicule = libelleVehicule;
    }

    public int getIdType() {
        return idType;
    }

    public void setIdType(int idType) {
        this.idType = idType;
    }

    public String getLibelleVehicule() {
        return libelleVehicule;
    }

    public void setLibelleVehicule(String libelleVehicule) {
        this.libelleVehicule = libelleVehicule;
    }
}
