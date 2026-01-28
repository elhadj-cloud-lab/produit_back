package com.bestech.produit.controller;

import com.bestech.bean.ProduitDTO;
import com.bestech.controller.ProduitApi;
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
public class ProduitController implements ProduitApi {

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
        List<Produit> produits = produitService.findByCategorieIdCategorie(idCategorie);
        return ResponseEntity.ok(produitBeanMapper.toDTO(produits));
    }

    @Override
    public ResponseEntity<List<ProduitDTO>> findByNomProduitContainingAndPrixProduitGreaterThan(String nom, Double prix) {
        List<Produit> produits = produitService.findByNomProduitContainingAndPrixProduitGreaterThan(nom, prix);
        return ResponseEntity.ok(produitBeanMapper.toDTO(produits));
    }

    @Override
    public ResponseEntity<List<ProduitDTO>> findByNomProduit(String nom) {
        List<Produit> produits = produitService.findByNomProduit(nom);
        return ResponseEntity.ok(produitBeanMapper.toDTO(produits));
    }

    @Override
    public ResponseEntity<List<ProduitDTO>> findByNomProduitContains(String nom) {
        List<Produit> produits = produitService.findByNomProduitContains(nom);
        return ResponseEntity.ok(produitBeanMapper.toDTO(produits));
    }

    @Override
    public ResponseEntity<List<ProduitDTO>> findByOrderByNomProduitAsc() {
        List<Produit> produits = produitService.findByOrderByNomProduitAsc();
        return ResponseEntity.ok(produitBeanMapper.toDTO(produits));
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
    public ResponseEntity<ProduitDTO> updateChampProduit(Long id, ProduitDTO produitDTO) {
        produitService.updateProduit(id, produitBeanMapper.toEntity(produitDTO));
        return ResponseEntity.ok(produitDTO);
    }

    @Override
    public ResponseEntity<ProduitDTO> updateProduit(Long id, ProduitDTO produitDTO) {
        produitService.updateProduit(id, produitBeanMapper.toEntity(produitDTO));
        return ResponseEntity.ok(produitDTO);
    }


}
