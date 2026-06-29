package com.bestech.produit.service;
import com.bestech.produit.model.Categorie;

import java.util.List;

public interface CategorieService {

    Categorie saveCategorie(Categorie p);
    Categorie getCategorieById(Long id);
    Categorie updateCategorie(Long id, Categorie p);
    Categorie patchCategorie(Long id, Categorie partial);
    void deleteCategorie(Long id);
    void deleteCategorie(Categorie p);
    List<Categorie> getAllCategories();
}
