package com.bestech.produit.service;

import com.bestech.produit.model.Produit;
import com.bestech.produit.repository.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public Produit updateProduit(Produit p) {
        return produitRepository.save(p);
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
}
