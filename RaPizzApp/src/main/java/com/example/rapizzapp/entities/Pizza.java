package com.example.rapizzapp.entities;

public class Pizza {
    private int idPizza;
    private String libellePizza;

    private String taillePizza;
    private double prix;

    public Pizza(){
        this.idPizza = -1;
        this.libellePizza = "";
        this.prix = -1;
        this.taillePizza = "";
    }

    public Pizza(int idPizza,String libellePizza,double prix, String taillePizza){
        this.idPizza = idPizza;
        this.libellePizza = libellePizza;
        this.prix = prix;
        this.taillePizza = taillePizza;
    }

    public int getIdPizza() {
        return idPizza;
    }

    public void setIdPizza(int idPizza) {
        this.idPizza = idPizza;
    }

    public String getLibellePizza() {
        return libellePizza;
    }

    public void setLibellePizza(String libellePizza) {
        this.libellePizza = libellePizza;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public String getTaillePizza() {
        return taillePizza;
    }

    public void setTaillePizza(String taillePizza) {
        this.taillePizza = taillePizza;
    }

    @Override
    public String toString(){
        return libellePizza;
    }
}

