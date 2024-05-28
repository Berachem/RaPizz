package com.example.rapizzapp.entities;

public class Ingredient {
    private int idIngredient;

    private String libelleIngredient;

    public Ingredient(int idIngredient, String libelleIngredient){
        this.idIngredient = idIngredient;
        this.libelleIngredient = libelleIngredient;
    }

    public String getLibelleIngredient() {
        return this.libelleIngredient;
    }

    public int getIdIngredient() {
        return this.idIngredient;
    }

    public void setIdIngredient(int idIngredient){
        this.idIngredient = idIngredient;
    }

    public void setLibelleIngredient(String libelleIngredient){
        this.libelleIngredient =  libelleIngredient;
    }
}
