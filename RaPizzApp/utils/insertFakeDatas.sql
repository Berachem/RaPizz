-- Script d'insertions de données dans les tables 
INSERT INTO Ingrédient(IdIngredient, libelleIngredient) VALUES
(1, 'Tomate'),
(2, 'Fromage'),
(3, 'Jambon'),
(4, 'Champignons'),
(5, 'Olives');

-- Insertion de données pour la table Pizza
INSERT INTO Pizza(IdPizza, LibellePizza, Prix, Image) VALUES
(1, 'Margherita', 10.00, 'https://www.dominospizza.pl/getmedia/35a49b5d-380a-45d3-9c1c-d245efe9e03a/520x520-margherita_2.png'),
(2, 'Reine', 12.00, 'https://www.leperceneige-arc1950.fr/wp-content/uploads/2020/12/reine.png'),
(3, 'Napolitaine', 11.00, 'https://www.leperceneige-arc1950.fr/wp-content/uploads/2020/12/napolitaine.png'),
(4, 'Quatre fromages', 13.00, 'https://pizzatime.fr/wp-content/uploads/2023/11/FROMAGES.png'),
(5, 'Calzone', 12.50, 'https://www.pizza-91.fr/img/entrees/d13f94e0530450ff4adc7b244125240a.png');

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
INSERT INTO Client(IdClient, Nom, Prenom, NumeroAbonnement, Solde, Role) VALUES
(1, 'Mike', 'Sophie', 12345, 50, 'USER'),
(2, 'Petit', 'Pierre', 12346, 75, 'USER'),
(3, 'Moreau', 'Juliette', 12347, 30, 'USER'),
(4, 'Mario', 'Luigi', 1, 75, 'ADMIN');


-- Insertion de données pour la table Vehicule
INSERT INTO Vehicule(IdVehicule, IdType, Nom) VALUES
(1, 1, 'Peugeot 206'),
(2, 2, 'Yamaha XJR1300'),
(3, 1, 'Citroen C3');

-- Insertion de données pour la table Commande
INSERT INTO Commande(adresseCommande, DateCommande, DateLivraison, IdClient, IdLivreur, IdVehicule) VALUES
('15 rue des Lilas, Montreuil 93100', '2023-04-01 18:30:00', '2023-04-01 19:15:00', 1, 1, 1),
('28 avenue de la République, Rosny 93110', '2023-04-02 19:00:00', '2023-04-02 19:45:00', 2, 2, 2),
('33 boulevard de la Liberté, Bondy 93140', '2023-04-03 12:30:00', '2023-04-03 13:20:00', 3, 3, 3),
('47 rue Pasteur, Noisy-le-Sec 93130', '2023-04-04 20:00:00', '2023-04-04 20:50:00', 1, 2, 1),
('59 avenue Gallieni, Bagnolet 93170', '2023-04-05 18:00:00', '2023-04-05 18:45:00', 2, 1, 2),
('11 rue de Paris, Vincennes 94300', '2023-04-06 19:30:00', '2023-04-06 20:15:00', 3, 3, 1),
('22 avenue du Château, Le Perreux 94170', '2023-04-07 12:45:00', '2023-04-07 13:30:00', 1, 2, 2),
('8 boulevard de Strasbourg, Saint-Maur 94100', '2023-04-08 20:30:00', '2023-04-08 21:10:00', 2, 1, 3),
('77 rue Jean Jaurès, Champigny 94500', '2023-04-09 18:20:00', '2023-04-09 19:05:00', 3, 3, 2),
('2 allée des Roses, Fontenay 94120', '2023-04-10 19:40:00', '2023-04-10 20:30:00', 1, 1, 3),
('90 rue Victor Hugo, Nogent 94130', '2023-04-11 18:10:00', '2023-04-11 18:55:00', 2, 2, 1),
('55 avenue Mozart, Joinville 94340', '2023-04-12 20:05:00', '2023-04-12 20:50:00', 3, 1, 2),
('6 rue des Peupliers, Neuilly-Plaisance 93360', '2023-04-13 19:50:00', '2023-04-13 20:35:00', 1, 3, 1),
('44 avenue de Verdun, Ivry 94200', '2023-04-14 18:30:00', '2023-04-14 19:15:00', 2, 2, 3),
('17 rue de l’Égalité, Villejuif 94800', '2023-04-15 20:00:00', '2023-04-15 20:45:00', 3, 1, 2);

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
(5, 3, 5),  -- Pizza Calzone de taille Ogresse dans la commande 5
(1, 2, 6),  -- Pizza Margherita de taille Humaine dans la commande 6
(2, 1, 7),  -- Pizza Reine de taille Naine dans la commande 7
(3, 2, 8),  -- Pizza Napolitaine de taille Humaine dans la commande 8
(4, 3, 9),  -- Pizza Quatre fromages de taille Ogresse dans la commande 9
(5, 1, 10), -- Pizza Calzone de taille Naine dans la commande 10
(1, 3, 11), -- Pizza Margherita de taille Ogresse dans la commande 11
(2, 2, 12), -- Pizza Reine de taille Humaine dans la commande 12
(3, 1, 13), -- Pizza Napolitaine de taille Naine dans la commande 13
(4, 2, 14); -- Pizza Quatre fromages de taille Humaine dans la commande 14

