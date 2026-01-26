package com.bestech.produit.controller;

import __package.projet_.bean.ProduitDTO;
import __package.projet_.controller.ProduitsApi;
import com.bestech.produit.bean.ProduitBeanMapper;
import com.bestech.produit.model.Produit;
import com.bestech.produit.service.ProduitService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
@AllArgsConstructor
public class ProduitController implements ProduitsApi {

    private final ProduitService produitService;
    private final ProduitBeanMapper produitBeanMapper;

    @Override
    public ResponseEntity<ProduitDTO> createProduit(ProduitDTO produitDTO) {
        produitService.saveProduit(produitBeanMapper.toEntity(produitDTO));
        return ResponseEntity.ok(produitDTO);
    }

    @Override
    public ResponseEntity<Void> deleteProduit(Long id) {
        produitService.deleteProduit(id);
        return null;
    }

    @Override
    public ResponseEntity<List<ProduitDTO>> findByCategorieIdCategorie(Long idCategorie) {
        return null;
    }

    @Override
    public ResponseEntity<List<ProduitDTO>> findByNomPrixProduit(String nom, Double prix) {
        return null;
    }

    @Override
    public ResponseEntity<List<ProduitDTO>> findByNomProduit(String nom) {
        return null;
    }

    @Override
    public ResponseEntity<List<ProduitDTO>> findByNomProduitContains(String nom) {
        return null;
    }

    @Override
    public ResponseEntity<List<ProduitDTO>> findByOrderByNomProduitAsc() {
        return null;
    }

    @Override
    public ResponseEntity<List<ProduitDTO>> getAllProduits() {

        List<Produit> produits = produitService.getAllProduits();
        return ResponseEntity.ok(produitBeanMapper.toDTO(produits));
    }

    @Override
    public ResponseEntity<ProduitDTO> getProduitById(Long id) {
        Produit produit = produitService.getProduitById(id);
        return ResponseEntity.ok(produitBeanMapper.toDTO(produit));
    }

    @Override
    public ResponseEntity<List<ProduitDTO>> trierProduitsNomsPrix() {
        List<Produit> produits = produitService.trierProduitsNomsPrix();
        return ResponseEntity.ok(produitBeanMapper.toDTO(produits));
    }

    @Override
    public ResponseEntity<ProduitDTO> updateProduit(Long id, ProduitDTO produitDTO) {
        produitService.updateProduit(id, produitBeanMapper.toEntity(produitDTO));
        return ResponseEntity.ok(produitDTO);
    }


}
