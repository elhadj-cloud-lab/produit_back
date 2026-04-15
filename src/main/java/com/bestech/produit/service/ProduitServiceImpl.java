package com.bestech.produit.service;

import com.bestech.produit.model.Categorie;
import com.bestech.produit.model.Produit;
import com.bestech.produit.repository.ImageRepository;
import com.bestech.produit.repository.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
class ProduitServiceImpl implements ProduitService {


    private final ProduitRepository produitRepository;

    @Autowired
    private ImageRepository imageRepository;

    private ProduitServiceImpl(ProduitRepository produitRepository) {

        this.produitRepository = produitRepository;
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Produit saveProduit(Produit p) {
        return produitRepository.save(p);
    }

    @Override
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Produit getProduitById(Long id) {

        return produitRepository.findById(id).get();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Produit updateProduit(Long id, Produit p) {

        return produitRepository.findById(id)
                .map(existingProduit -> {
                    existingProduit.setNomProduit(p.getNomProduit());
                    existingProduit.setPrixProduit(p.getPrixProduit());
                    existingProduit.setDateCreation(p.getDateCreation());
                    existingProduit.setCategorie(p.getCategorie());
                    return produitRepository.save(existingProduit);
                })
                .orElseThrow(() -> new NoSuchElementException("Le produit avec l'id " + id + " non trouvé."));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteProduit(Long id) {
        imageRepository.deleteByProduitIdProduit(id);
        produitRepository.deleteById(id);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteProduit(Produit p) {

        produitRepository.delete(p);
    }

    @Override
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<Produit> getAllProduits() {

        return produitRepository.findAll();
    }

    @Override
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<Produit> findByNomProduit(String nomProduit) {
        return produitRepository.findByNomProduit(nomProduit);
    }

    @Override
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<Produit> findByNomProduitContains(String nomProduit) {
        return produitRepository.findByNomProduitContains(nomProduit);
    }

    @Override
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<Produit> findByNomProduitContainingAndPrixProduitGreaterThan(String nom, Double prix) {
        return produitRepository.findByNomProduitContainingAndPrixProduitGreaterThan(nom, prix);
    }

    @Override
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<Produit> findByCategorie(Categorie categorie) {

        return findByCategorie(categorie);
    }

    @Override
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<Produit> findByCategorieIdCategorie(Long idCat) {
        return produitRepository.findByCategorieIdCategorie(idCat);
    }

    @Override
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<Produit> findByOrderByNomProduitAsc() {

        return produitRepository.findByOrderByNomProduitAsc();
    }

    @Override
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<Produit> trierProduitsNomsPrix() {

        return produitRepository.trierProduitsNomsPrix();
    }
}
