package com.bestech.produit.service;

import com.bestech.produit.model.Produit;
import java.util.List;

public interface ProduitService {

    Produit saveProduit(Produit p);
    Produit getProduitById(Long id);
    Produit updateProduit(Produit p);
    void deleteProduit(Long id);
    void deleteProduit(Produit p);
    List<Produit> getAllProduits();

}
