package com.bestech.produit;
import com.bestech.produit.model.Categorie;
import com.bestech.produit.model.Produit;
import com.bestech.produit.repository.ProduitRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
class ProduitApplicationTests {

    @Autowired
    private ProduitRepository produitRepository;

	@Test
	void testCreateProduit() {
		Produit p = new Produit();

        p.setNomProduit("Iphone 17");
        p.setPrixProduit(1200.99);
        p.setDateCreation(new Date());

        produitRepository.save(p);

        Produit p2 = new Produit();
        p2.setNomProduit("Iphone 13");
        p2.setPrixProduit(500.0);
        p2.setDateCreation(new Date());

        produitRepository.save(p2);
	}

    @Test
    void testCreateProduit2() {
        Produit p = new Produit();

        p.setNomProduit("PC Mac Boot Pro");
        p.setPrixProduit(5000.99);
        p.setDateCreation(new Date());

        produitRepository.save(p);
    }

    @Test
    void testFindByIdProduit() {
        Produit p = produitRepository.findById(1L).get();
        System.out.println(p);
    }

    @Test
    void testFindAllProduit() {
        List<Produit> produits = produitRepository.findAll();
        for (Produit p : produits) {
            System.out.println(p);
        }
    }

    @Test
    void testUpdateProduit() {
        Produit p = produitRepository.findById(1L).get();
        p.setNomProduit("Iphone 17 Pro Max");
        p.setPrixProduit(1300.99);
        produitRepository.save(p);
    }

    @Test
    void testDeleteProduit() {
        Produit p = produitRepository.findById(2L).get();
        produitRepository.delete(p);
    }

    @Test
    void testFindByNomProduit() {
        List<Produit> produits = produitRepository.findByNomProduit("Iphone 13");
        afficherListeProduit(produits);
    }

    @Test
    void testFindByNomProduitContains() {
        List<Produit> produits = produitRepository.findByNomProduitContains("Iphone");
        afficherListeProduit(produits);
    }

    @Test
    public void testFindByNomPrixProduit() {
        List<Produit> produits = produitRepository.findByNomPrixProduit("Iphone 13",100.0);
        afficherListeProduit(produits);
    }

    @Test
    void testFindByCategorie() {
        Categorie categorie = new Categorie();
        categorie.setIdCategorie(2L);

        List<Produit> produits = produitRepository.findByCategorie(categorie);
        afficherListeProduit(produits);
    }

    @Test
    void testFindByCategorieIdCat() {

        List<Produit> produits = produitRepository.findByCategorieIdCategorie(1L);
        afficherListeProduit(produits);
    }

    @Test
    void testFindByOrderByNomProduitAsc() {

        List<Produit> produits = produitRepository.findByOrderByNomProduitAsc();
        afficherListeProduit(produits);
    }

    @Test
    void testTrierProduitsNomsPrix() {

        List<Produit> produits = produitRepository.trierProduitsNomsPrix();
        afficherListeProduit(produits);
    }

    private void afficherListeProduit(List<Produit> produits) {
        for (Produit p : produits) {
            System.out.println(p);
        }
    }
}
