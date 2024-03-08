CREATE TABLE Ingrédient(
   IdIngredient INT,
   libelleIngredient VARCHAR(50),
   PRIMARY KEY(IdIngredient)
);

CREATE TABLE Pizza(
   IdPizza INT,
   LibellePizza VARCHAR(50),
   Prix DECIMAL(15,2),
   PRIMARY KEY(IdPizza)
);

CREATE TABLE Taille(
   idTaille INT,
   LibelleTaille VARCHAR(50),
   ModificateurPrix VARCHAR(50),
   PRIMARY KEY(idTaille)
);

CREATE TABLE TypeVehicule(
   IdType INT,
   LibelleVehicule VARCHAR(50),
   PRIMARY KEY(IdType)
);

CREATE TABLE Livreur(
   IdLivreur INT,
   Nom VARCHAR(50),
   Prenom VARCHAR(50),
   PRIMARY KEY(IdLivreur)
);

CREATE TABLE Client(
   IdClient INT,
   Nom VARCHAR(50),
   Prenom VARCHAR(50),
   NumeroAbonnement INT,
   Solde INT,
   PRIMARY KEY(IdClient)
);

CREATE TABLE Vehicule(
   IdVehicule INT,
   IdType INT NOT NULL,
   Nom VARCHAR(255),
   PRIMARY KEY(IdVehicule),
   FOREIGN KEY(IdType) REFERENCES TypeVehicule(IdType)
);

CREATE TABLE Commande(
   idCommande INT,
   DateCommande DATETIME,
   DateLivraison DATETIME,
   IdClient INT NOT NULL,
   IdLivreur INT NOT NULL,
   IdVehicule DATE NOT NULL,
   PRIMARY KEY(idCommande),
   FOREIGN KEY(IdClient) REFERENCES Client(IdClient),
   FOREIGN KEY(IdLivreur) REFERENCES Livreur(IdLivreur),
   FOREIGN KEY(IdVehicule) REFERENCES Vehicule(IdVehicule)
);

CREATE TABLE Compose(
   IdIngredient INT,
   IdPizza INT,
   PRIMARY KEY(IdIngredient, IdPizza),
   FOREIGN KEY(IdIngredient) REFERENCES Ingrédient(IdIngredient),
   FOREIGN KEY(IdPizza) REFERENCES Pizza(IdPizza)
);

CREATE TABLE Contient(
   IdPizza INT,
   idTaille INT,
   idCommande INT,
   PRIMARY KEY(IdPizza, idTaille, idCommande),
   FOREIGN KEY(IdPizza) REFERENCES Pizza(IdPizza),
   FOREIGN KEY(idTaille) REFERENCES Taille(idTaille),
   FOREIGN KEY(idCommande) REFERENCES Commande(idCommande)
);
