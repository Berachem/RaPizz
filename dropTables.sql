-- Suppression des tables avec des dépendances en premier
DROP TABLE IF EXISTS Contient;
DROP TABLE IF EXISTS Compose;
DROP TABLE IF EXISTS Commande;
DROP TABLE IF EXISTS Vehicule;
DROP TABLE IF EXISTS Client;
DROP TABLE IF EXISTS Livreur;

-- Ensuite, supprimer les tables référencées
DROP TABLE IF EXISTS Pizza;
DROP TABLE IF EXISTS Taille;
DROP TABLE IF EXISTS Ingrédient;
DROP TABLE IF EXISTS TypeVehicule;
