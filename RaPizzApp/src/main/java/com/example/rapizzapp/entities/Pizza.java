package com.example.rapizzapp.entities;

public class Pizza {
    private int idPizza;
    private String libellePizza;
    private double prix;

    public Pizza(){
        this.idPizza = -1;
        this.libellePizza = "";
        this.prix = -1;
    }

    public Pizza(int idPizza,String libellePizza,double prix){
        this.idPizza = idPizza;
        this.libellePizza = libellePizza;
        this.prix = prix;
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

    @Override
    public String toString(){
        return libellePizza;
    }
}

