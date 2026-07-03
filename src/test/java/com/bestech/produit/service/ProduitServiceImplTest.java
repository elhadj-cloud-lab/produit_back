package com.bestech.produit.service;

import com.bestech.produit.model.Categorie;
import com.bestech.produit.model.Produit;
import com.bestech.produit.repository.ImageRepository;
import com.bestech.produit.repository.ProduitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProduitServiceImplTest {

    @Mock ProduitRepository produitRepository;
    @Mock ImageRepository imageRepository;

    @InjectMocks ProduitServiceImpl produitService;

    @BeforeEach
    void injectImageRepository() {
        // ProduitServiceImpl uses constructor injection for ProduitRepository
        // and field injection for ImageRepository. Mockito stops after constructor
        // injection, so we inject imageRepository manually.
        ReflectionTestUtils.setField(produitService, "imageRepository", imageRepository);
    }

    private Produit buildProduit(Long id, String nom, double prix) {
        Produit p = new Produit();
        p.setIdProduit(id);
        p.setNomProduit(nom);
        p.setPrixProduit(prix);
        p.setDateCreation(new Date());
        return p;
    }

    @Test
    void saveProduit_delegatesToRepository() {
        Produit produit = buildProduit(null, "MacBook", 1299.99);
        when(produitRepository.save(produit)).thenReturn(produit);

        Produit result = produitService.saveProduit(produit);

        assertThat(result).isSameAs(produit);
        verify(produitRepository).save(produit);
    }

    @Test
    void getProduitById_returnsProduit_whenFound() {
        Produit produit = buildProduit(1L, "iPhone", 999.0);
        when(produitRepository.findById(1L)).thenReturn(Optional.of(produit));

        Produit result = produitService.getProduitById(1L);

        assertThat(result.getNomProduit()).isEqualTo("iPhone");
    }

    @Test
    void getAllProduits_returnsAllFromRepository() {
        when(produitRepository.findAll()).thenReturn(List.of(
                buildProduit(1L, "P1", 10.0),
                buildProduit(2L, "P2", 20.0)
        ));

        assertThat(produitService.getAllProduits()).hasSize(2);
    }

    @Test
    void updateProduit_updatesFieldsAndSaves() {
        Produit existing = buildProduit(1L, "OldName", 100.0);
        Categorie cat = new Categorie();
        cat.setIdCategorie(5L);

        Produit update = buildProduit(null, "NewName", 200.0);
        update.setCategorie(cat);

        when(produitRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(produitRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Produit result = produitService.updateProduit(1L, update);

        assertThat(result.getNomProduit()).isEqualTo("NewName");
        assertThat(result.getPrixProduit()).isEqualTo(200.0);
        assertThat(result.getCategorie()).isSameAs(cat);
    }

    @Test
    void updateProduit_throwsNoSuchElement_whenNotFound() {
        when(produitRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> produitService.updateProduit(99L, new Produit()))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void deleteProduit_byId_deletesImagesFirstThenProduit() {
        produitService.deleteProduit(1L);

        verify(imageRepository).deleteByProduitIdProduit(1L);
        verify(produitRepository).deleteById(1L);
    }

    @Test
    void deleteProduit_byEntity_delegatesToRepository() {
        Produit produit = buildProduit(1L, "ToDelete", 50.0);
        produitService.deleteProduit(produit);
        verify(produitRepository).delete(produit);
    }

    @Test
    void findByNomProduit_returnsMatchingProduits() {
        Produit p = buildProduit(1L, "Samsung S24", 799.0);
        when(produitRepository.findByNomProduit("Samsung S24")).thenReturn(List.of(p));

        List<Produit> result = produitService.findByNomProduit("Samsung S24");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNomProduit()).isEqualTo("Samsung S24");
    }

    @Test
    void findByNomProduitContains_returnsMatchingProduits() {
        when(produitRepository.findByNomProduitContains("Samsung")).thenReturn(
                List.of(buildProduit(1L, "Samsung S24", 799.0))
        );

        assertThat(produitService.findByNomProduitContains("Samsung")).hasSize(1);
    }

    @Test
    void findByNomProduitContainingAndPrixProduitGreaterThan_filtersCorrectly() {
        when(produitRepository.findByNomProduitContainingAndPrixProduitGreaterThan("Mac", 500.0))
                .thenReturn(List.of(buildProduit(1L, "MacBook Pro", 1999.0)));

        List<Produit> result = produitService.findByNomProduitContainingAndPrixProduitGreaterThan("Mac", 500.0);

        assertThat(result).hasSize(1);
    }

    @Test
    void findByCategorieIdCategorie_returnsProduitsForCategory() {
        when(produitRepository.findByCategorieIdCategorie(3L)).thenReturn(
                List.of(buildProduit(1L, "Laptop", 900.0))
        );

        assertThat(produitService.findByCategorieIdCategorie(3L)).hasSize(1);
    }

    @Test
    void findByOrderByNomProduitAsc_returnsSortedProduits() {
        when(produitRepository.findByOrderByNomProduitAsc()).thenReturn(
                List.of(
                        buildProduit(2L, "Apple Watch", 399.0),
                        buildProduit(1L, "iPhone", 999.0)
                )
        );

        List<Produit> result = produitService.findByOrderByNomProduitAsc();

        assertThat(result).extracting(Produit::getNomProduit)
                .containsExactly("Apple Watch", "iPhone");
    }

    @Test
    void trierProduitsNomsPrix_returnsRepositoryResult() {
        when(produitRepository.trierProduitsNomsPrix()).thenReturn(
                List.of(buildProduit(1L, "Sony TV", 1500.0))
        );

        assertThat(produitService.trierProduitsNomsPrix()).hasSize(1);
    }
}
