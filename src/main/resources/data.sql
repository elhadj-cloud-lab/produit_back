-- Données de test pour les produits
DELETE FROM produit;

-- Nettoie d'abord (optionnel en H2)
DELETE FROM produit;

-- Insère des données de test (sans spécifier l'ID auto-généré)
INSERT INTO produit (nom_produit, prix_produit, date_creation) VALUES
                                                                   ('Produit A', 99.99, '2024-01-01'),
                                                                   ('Produit B', 149.99, '2024-01-01'),
                                                                   ('Produit C', 199.99, '2024-01-01');

-- Note : Les IDs seront 1, 2, 3 automatiquement (IDENTITY)