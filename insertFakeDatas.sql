-- Script d'insertions de données dans les tables 
INSERT INTO Ingrédient(IdIngredient, libelleIngredient) VALUES
(1, 'Tomate'),
(2, 'Fromage'),
(3, 'Jambon'),
(4, 'Champignons'),
(5, 'Olives');


INSERT INTO Pizza(IdPizza, LibellePizza, Prix) VALUES
(1, 'Margherita', 10.00),
(2, 'Reine', 12.00),
(3, 'Napolitaine', 11.00),
(4, 'Quatre fromages', 13.00),
(5, 'Calzone', 12.50);


INSERT INTO Taille(idTaille, LibelleTaille, ModificateurPrix) VALUES
(1, 'Naine', '0.67'),
(2, 'Humaine', '1.00'),
(3, 'Ogresse', '1.33');


INSERT INTO TypeVehicule(IdType, LibelleVehicule) VALUES
(1, 'Voiture'),
(2, 'Moto');


INSERT INTO Livreur(IdLivreur, Nom, Prenom) VALUES
(1, 'Dupont', 'Jean'),
(2, 'Martin', 'Luc'),
(3, 'Durand', 'Marie');


INSERT INTO Client(IdClient, Nom, Prenom, NumeroAbonnement, Solde) VALUES
(1, 'Lefebvre', 'Sophie', 12345, 50),
(2, 'Petit', 'Pierre', 12346, 75),
(3, 'Moreau', 'Juliette', 12347, 30);
