-- Script d'insertions de données dans les tables 
INSERT INTO Ingrédient(IdIngredient, libelleIngredient) VALUES
(1, 'Tomate'),
(2, 'Fromage'),
(3, 'Jambon'),
(4, 'Champignons'),
(5, 'Olives');

-- Insertion de données pour la table Pizza
INSERT INTO Pizza(IdPizza, LibellePizza, Prix) VALUES
(1, 'Margherita', 10.00),
(2, 'Reine', 12.00),
(3, 'Napolitaine', 11.00),
(4, 'Quatre fromages', 13.00),
(5, 'Calzone', 12.50);

-- Insertion de données pour la table Taille
INSERT INTO Taille(idTaille, LibelleTaille, ModificateurPrix) VALUES
(1, 'Naine', '0.67'),
(2, 'Humaine', '1.00'),
(3, 'Ogresse', '1.33');

-- Insertion de données pour la table TypeVehicule
INSERT INTO TypeVehicule(IdType, LibelleVehicule) VALUES
(1, 'Voiture'),
(2, 'Moto');

-- Insertion de données pour la table Livreur
INSERT INTO Livreur(IdLivreur, Nom, Prenom) VALUES
(1, 'Dupont', 'Jean'),
(2, 'Martin', 'Luc'),
(3, 'Durand', 'Marie');

-- Insertion de données pour la table Client
INSERT INTO Client(IdClient, Nom, Prenom, NumeroAbonnement, Solde) VALUES
(1, 'Mike', 'Sophie', 12345, 50),
(2, 'Petit', 'Pierre', 12346, 75),
(3, 'Moreau', 'Juliette', 12347, 30);


-- Insertion de données pour la table Vehicule
INSERT INTO Vehicule(IdVehicule, IdType, Nom) VALUES
(1, 1, 'Peugeot 206'),
(2, 2, 'Yamaha XJR1300'),
(3, 1, 'Citroen C3');

-- Insertion de données pour la table Commande
INSERT INTO Commande(idCommande, adresseCommande, DateCommande, DateLivraison, IdClient, IdLivreur, IdVehicule) VALUES
(1, '48 avenue ronsard, Gagny 93220','2023-03-08 18:00:00', '2023-03-08 19:00:00', 1, 1, 1),
(2, '74 rue martin, Chelles 77500', '2023-03-09 12:00:00', '2023-03-09 13:00:00', 2, 2, 2),
(3, '12 avenue paul, Aulnay-sous-bois 93330', '2023-03-10 20:00:00', '2023-03-10 21:00:00', 3, 3, 3);

-- Insertion de données pour la table Compose
-- Associant chaque pizza à ses ingrédients
INSERT INTO Compose(IdIngredient, IdPizza) VALUES
(1, 1), -- Tomate pour Margherita
(2, 1), -- Fromage pour Margherita
(1, 2), -- Tomate pour Reine
(2, 2), -- Fromage pour Reine
(3, 2), -- Jambon pour Reine
(4, 2); -- Champignons pour Reine

-- Insertion de données pour la table Contient
-- Détails des commandes (quelle pizza, quelle taille, quelle commande)
INSERT INTO Contient(IdPizza, idTaille, idCommande) VALUES
(1, 1, 1), -- Pizza Margherita de taille Naine dans la commande 1
(2, 2, 2), -- Pizza Reine de taille Humaine dans la commande 2
(3, 3, 3); -- Pizza Napolitaine de taille Ogresse dans la commande 3
