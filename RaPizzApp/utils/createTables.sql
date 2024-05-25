CREATE TABLE Ingrédient(
   IdIngredient INT AUTO_INCREMENT,
   libelleIngredient VARCHAR(50),
   PRIMARY KEY(IdIngredient)
);

CREATE TABLE Pizza(
   IdPizza INT AUTO_INCREMENT,
   LibellePizza VARCHAR(50),
   Prix DECIMAL(15,2),
   Image VARCHAR(200),
   PRIMARY KEY(IdPizza)
);

CREATE TABLE Taille(
   idTaille INT AUTO_INCREMENT,
   LibelleTaille VARCHAR(50),
   ModificateurPrix VARCHAR(50),
   PRIMARY KEY(idTaille)
);

CREATE TABLE TypeVehicule(
   IdType INT AUTO_INCREMENT,
   LibelleVehicule VARCHAR(50),
   PRIMARY KEY(IdType)
);

CREATE TABLE Livreur(
   IdLivreur INT AUTO_INCREMENT,
   Nom VARCHAR(50),
   Prenom VARCHAR(50),
   PRIMARY KEY(IdLivreur)
);

CREATE TABLE Client(
   IdClient INT AUTO_INCREMENT,
   Nom VARCHAR(50),
   Prenom VARCHAR(50),
   NumeroAbonnement INT UNIQUE,
   Solde INT,
   Role ENUM('USER', 'ADMIN') NOT NULL DEFAULT 'USER',
   PRIMARY KEY(IdClient)
);

CREATE TABLE Vehicule(
   IdVehicule INT AUTO_INCREMENT,
   IdType INT NOT NULL,
   Nom VARCHAR(255),
   PRIMARY KEY(IdVehicule),
   FOREIGN KEY(IdType) REFERENCES TypeVehicule(IdType)
);

CREATE TABLE Commande(
   idCommande INT AUTO_INCREMENT,
   adresseCommande VARCHAR(500),
   DateCommande DATETIME,
   DateLivraison DATETIME,
   gratuit BOOL DEFAULT 0,
   IdClient INT NOT NULL,
   IdLivreur INT NOT NULL,
   IdVehicule INT NOT NULL,
   PRIMARY KEY(idCommande),
   FOREIGN KEY(IdClient) REFERENCES Client(IdClient),
   FOREIGN KEY(IdLivreur) REFERENCES Livreur(IdLivreur),
   FOREIGN KEY(IdVehicule) REFERENCES Vehicule(IdVehicule)
);

CREATE TABLE Compose(
   IdIngredient INT AUTO_INCREMENT,
   IdPizza INT,
   PRIMARY KEY(IdIngredient, IdPizza),
   FOREIGN KEY(IdIngredient) REFERENCES Ingrédient(IdIngredient),
   FOREIGN KEY(IdPizza) REFERENCES Pizza(IdPizza)
);

CREATE TABLE Contient(
   IdPizza INT AUTO_INCREMENT,
   idTaille INT,
   idCommande INT,
   gratuit BOOL DEFAULT 0,
   PRIMARY KEY(IdPizza, idTaille, idCommande),
   FOREIGN KEY(IdPizza) REFERENCES Pizza(IdPizza),
   FOREIGN KEY(idTaille) REFERENCES Taille(idTaille),
   FOREIGN KEY(idCommande) REFERENCES Commande(idCommande)
);
