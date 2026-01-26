package com.bestech.produit.service;

import com.bestech.produit.model.Categorie;
import com.bestech.produit.model.Produit;
import com.bestech.produit.repository.ProduitRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
class ProduitServiceImpl implements ProduitService {


    private final ProduitRepository produitRepository;

    private ProduitServiceImpl(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    @Override
    public Produit saveProduit(Produit p) {
        return produitRepository.save(p);
    }

    @Override
    public Produit getProduitById(Long id) {
        return produitRepository.findById(id).get();
    }

    @Override
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
    public void deleteProduit(Long id) {
        produitRepository.deleteById(id);
    }

    @Override
    public void deleteProduit(Produit p) {
        produitRepository.delete(p);
    }

    @Override
    public List<Produit> getAllProduits() {
        return produitRepository.findAll();
    }

    @Override
    public List<Produit> findByNomProduit(String nomProduit) {
        return produitRepository.findByNomProduit(nomProduit);
    }

    @Override
    public List<Produit> findByNomProduitContains(String nomProduit) {
        return produitRepository.findByNomProduitContains(nomProduit);
    }

    @Override
    public List<Produit> findByNomPrixProduit(String nomPrixProduit, Double prixProduit) {
        return produitRepository.findByNomPrixProduit(nomPrixProduit, prixProduit);
    }

    @Override
    public List<Produit> findByCategorie(Categorie categorie) {
        return findByCategorie(categorie);
    }

    @Override
    public List<Produit> findByCategorieIdCategorie(Long idCat) {
        return produitRepository.findByCategorieIdCategorie(idCat);
    }

    @Override
    public List<Produit> findByOrderByNomProduitAsc() {
        return produitRepository.findByOrderByNomProduitAsc();
    }

    @Override
    public List<Produit> trierProduitsNomsPrix() {
        return produitRepository.trierProduitsNomsPrix();
    }
}
