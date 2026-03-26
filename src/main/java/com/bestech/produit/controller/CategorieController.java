package com.bestech.produit.controller;


import com.bestech.bean.CategorieDTO;
import com.bestech.controller.CategorieApi;
import com.bestech.produit.bean.ProduitBeanMapper;
import com.bestech.produit.model.Categorie;
import com.bestech.produit.repository.CategorieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class CategorieController implements CategorieApi {

    @Autowired
    private CategorieRepository categorieRepository;
    @Autowired
    private ProduitBeanMapper produitBeanMapper;

    @Override
    public ResponseEntity<CategorieDTO> createCategorie(CategorieDTO categorieDTO) {
        return null;
    }

    @Override
    public ResponseEntity<Void> deleteCategorie(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<List<CategorieDTO>> getAllCategories() {
        List<Categorie> categories = categorieRepository.findAll();
        return ResponseEntity.ok(produitBeanMapper.toCategorieDTO(categories));
    }

    @Override
    public ResponseEntity<CategorieDTO> getCategorieById(Long id) {
        Categorie categorie = categorieRepository.findById(id).get();
        return ResponseEntity.ok(produitBeanMapper.toCategorieDTO(categorie));
    }

    @Override
    public ResponseEntity<CategorieDTO> updateCategorie(Long id, CategorieDTO categorieDTO) {
        return null;
    }

    @Override
    public ResponseEntity<CategorieDTO> updateChampCategorie(Long id, CategorieDTO categorieDTO) {
        return null;
    }
}
