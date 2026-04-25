package com.bestech.produit.controller;

import com.bestech.bean.CategorieDTO;
import com.bestech.controller.CategorieApi;
import com.bestech.produit.bean.ProduitBeanMapper;
import com.bestech.produit.model.Categorie;
import com.bestech.produit.service.CategorieService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
@AllArgsConstructor
public class CategorieController implements CategorieApi {

    private final CategorieService categorieService;
    private final ProduitBeanMapper produitBeanMapper;

    @Override
    public ResponseEntity<CategorieDTO> createCategorie(CategorieDTO categorieDTO) {
        categorieService.saveCategorie(produitBeanMapper.toCategorieEntity(categorieDTO));
        return ResponseEntity.ok((categorieDTO));
    }

    @Override
    public ResponseEntity<Void> deleteCategorie(Long id) {
        categorieService.deleteCategorie(id);
        return null;
    }

    @Override
    public ResponseEntity<List<CategorieDTO>> getAllCategories() {
        List<Categorie> categories = categorieService.getAllCategories();
        return ResponseEntity.ok(produitBeanMapper.toCategorieDTO(categories));
    }

    @Override
    public ResponseEntity<CategorieDTO> getCategorieById(Long id) {
        Categorie categorie = categorieService.getCategorieById(id);
        return ResponseEntity.ok(produitBeanMapper.toCategorieDTO(categorie));
    }

    @Override
    public ResponseEntity<CategorieDTO> updateCategorie(Long id, CategorieDTO categorieDTO) {
        categorieService.updateCategorie(id, produitBeanMapper.toCategorieEntity(categorieDTO));
        return ResponseEntity.ok((categorieDTO));
    }

    @Override
    public ResponseEntity<CategorieDTO> updateChampCategorie(Long id, CategorieDTO categorieDTO) {

        return null;
    }
}
