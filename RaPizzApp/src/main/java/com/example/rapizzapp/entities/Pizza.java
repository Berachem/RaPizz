package com.example.rapizzapp.entities;

import java.util.List;

public class Pizza {
    private int idPizza;
    private String libellePizza;

    private String taillePizza;

    private String imageURL;

    private List<Ingredient> ingredients;
    private double prix;

    private boolean isGratuit;

    public Pizza(){
        this.idPizza = -1;
        this.libellePizza = "";
        this.prix = -1;
        this.taillePizza = "";
        this.imageURL="";
        this.isGratuit = false;
    }

    public Pizza(int idPizza,String libellePizza,double prix, String taillePizza, String imageURL, List<Ingredient> ingredients){
        this.idPizza = idPizza;
        this.libellePizza = libellePizza;
        this.prix = prix;
        this.taillePizza = taillePizza;
        this.imageURL= imageURL;
        this.ingredients = ingredients;
        this.isGratuit = false;
    }

    public Pizza(int idPizza,String libellePizza,double prix, String taillePizza, String imageURL, List<Ingredient> ingredients,boolean isGratuit){
        this.idPizza = idPizza;
        this.libellePizza = libellePizza;
        this.prix = prix;
        this.taillePizza = taillePizza;
        this.imageURL = imageURL;
        this.ingredients = ingredients;
        this.isGratuit = isGratuit;
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

    public void setImagePizza(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getImagePizza() {
        return imageURL;
    }

    public boolean isGratuit(){
        return isGratuit;
    }

    public void setTaillePizza(String taillePizza) {
        this.taillePizza = taillePizza;
    }

    public List<String> getLibelleIngredients() {
        List<String> libelleIngredients = ingredients.stream().map(
                ingredient -> ingredient.getLibelleIngredient()
        ).toList();
        return libelleIngredients;
    }

    public List<Ingredient> getIngredients(){
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void setGratuit(boolean isGratuit){
        this.isGratuit = isGratuit;
    }

    @Override
    public String toString(){
        return libellePizza;
    }
}

