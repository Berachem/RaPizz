package com.example.rapizzapp.entities;

public class Taille {
    private int idTaille;
    private String libelleTaille;
    private String modificateurPrix;

    public Taille(int idTaille,String libelleTaille,String modificateurPrix){
        this.idTaille=idTaille;
        this.libelleTaille=libelleTaille;
        this.modificateurPrix=modificateurPrix;
    }

    public int getIdTaille() {
        return idTaille;
    }

    public void setIdTaille(int idTaille) {
        this.idTaille = idTaille;
    }

    public String getLibelleTaille() {
        return libelleTaille;
    }

    public void setLibelleTaille(String libelleTaille) {
        this.libelleTaille = libelleTaille;
    }

    public String getModificateurPrix() {
        return modificateurPrix;
    }

    public void setModificateurPrix(String modificateurPrix) {
        this.modificateurPrix = modificateurPrix;
    }

    @Override
    public String toString(){
        return libelleTaille;
    }
}

