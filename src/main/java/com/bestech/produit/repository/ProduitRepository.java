package com.bestech.produit.repository;

import com.bestech.produit.model.Categorie;
import com.bestech.produit.model.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProduitRepository extends JpaRepository<Produit, Long> {

    List<Produit> findByNomProduit(String nomProduit);
    List<Produit> findByNomProduitContains(String nomProduit);

    @Query("select p from Produit p where p.nomProduit like concat('%', :nom, '%') and p.prixProduit > :prix")
    List<Produit> findByNomProduitContainingAndPrixProduitGreaterThan(@Param("nom") String nom, @Param("prix") Double prix);

    @Query("select p from Produit p where p.categorie = ?1")
    List<Produit> findByCategorie(Categorie categorie);

    List<Produit> findByCategorieIdCategorie(Long idCat);

    List<Produit> findByOrderByNomProduitAsc();

    @Query("select p from Produit p order by p.nomProduit ASC, p.prixProduit DESC")
    List<Produit> trierProduitsNomsPrix();

}
