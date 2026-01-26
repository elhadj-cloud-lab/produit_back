package com.bestech.produit.service;

import com.bestech.produit.model.Categorie;
import com.bestech.produit.model.Produit;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProduitService {

    Produit saveProduit(Produit p);
    Produit getProduitById(Long id);
    Produit updateProduit(Long id, Produit p);
    void deleteProduit(Long id);
    void deleteProduit(Produit p);
    List<Produit> getAllProduits();

    List<Produit> findByNomProduit(String nomProduit);
    List<Produit> findByNomProduitContains(String nomProduit);
    List<Produit> findByNomPrixProduit(@Param("nom") String nomPrixProduit, @Param("prix") Double prixProduit);
    List<Produit> findByCategorie(Categorie categorie);
    List<Produit> findByCategorieIdCategorie(Long idCat);
    List<Produit> findByOrderByNomProduitAsc();
    List<Produit> trierProduitsNomsPrix();

}
