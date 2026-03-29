package com.bestech.produit.service;

import com.bestech.produit.model.Categorie;
import com.bestech.produit.repository.CategorieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
@Service
public class CategorieServiceImpl implements CategorieService {

    private final CategorieRepository categorieRepository;

    private CategorieServiceImpl(CategorieRepository categorieRepository) {
        this.categorieRepository = categorieRepository;
    }

    @Override
    public Categorie saveCategorie(Categorie c) {
        return categorieRepository.save(c);
    }

    @Override
    public Categorie getCategorieById(Long id) {
        return categorieRepository.findById(id).get();
    }

    @Override
    public Categorie updateCategorie(Long id, Categorie c) {
        return categorieRepository.findById(id)
                .map(existingCetegorie -> {
                    existingCetegorie.setNomCategorie(c.getNomCategorie());
                    existingCetegorie.setDescription(c.getDescription());
                    return categorieRepository.save(existingCetegorie);
                })
                .orElseThrow(() -> new NoSuchElementException("La categorie avec l'id " + id + " non trouvé."));
    }

    @Override
    public void deleteCategorie(Long id) {
        categorieRepository.deleteById(id);
    }

    @Override
    public void deleteCategorie(Categorie p) {
        categorieRepository.delete(p);
    }

    @Override
    public List<Categorie> getAllCategories() {
        return categorieRepository.findAll();
    }
}
