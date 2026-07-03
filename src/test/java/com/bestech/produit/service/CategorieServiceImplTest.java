package com.bestech.produit.service;

import com.bestech.produit.model.Categorie;
import com.bestech.produit.repository.CategorieRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategorieServiceImplTest {

    @Mock CategorieRepository categorieRepository;
    @InjectMocks CategorieServiceImpl categorieService;

    private Categorie buildCategorie(Long id, String nom) {
        Categorie c = new Categorie();
        c.setIdCategorie(id);
        c.setNomCategorie(nom);
        c.setDescription("desc-" + nom);
        return c;
    }

    @Test
    void saveCategorie_delegatesToRepository() {
        Categorie cat = buildCategorie(null, "Électronique");
        when(categorieRepository.save(cat)).thenReturn(cat);

        Categorie result = categorieService.saveCategorie(cat);

        assertThat(result).isSameAs(cat);
        verify(categorieRepository).save(cat);
    }

    @Test
    void getCategorieById_returnsCategorie_whenFound() {
        Categorie cat = buildCategorie(1L, "Informatique");
        when(categorieRepository.findById(1L)).thenReturn(Optional.of(cat));

        Categorie result = categorieService.getCategorieById(1L);

        assertThat(result.getNomCategorie()).isEqualTo("Informatique");
    }

    @Test
    void getAllCategories_returnsFullList() {
        when(categorieRepository.findAll()).thenReturn(List.of(
                buildCategorie(1L, "Cat1"),
                buildCategorie(2L, "Cat2")
        ));

        assertThat(categorieService.getAllCategories()).hasSize(2);
    }

    @Test
    void updateCategorie_updatesFieldsAndSaves() {
        Categorie existing = buildCategorie(1L, "OldName");
        Categorie update = buildCategorie(null, "NewName");
        update.setDescription("NewDesc");

        when(categorieRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(categorieRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Categorie result = categorieService.updateCategorie(1L, update);

        assertThat(result.getNomCategorie()).isEqualTo("NewName");
        assertThat(result.getDescription()).isEqualTo("NewDesc");
    }

    @Test
    void updateCategorie_throwsNoSuchElement_whenNotFound() {
        when(categorieRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categorieService.updateCategorie(99L, new Categorie()))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void patchCategorie_updatesOnlyNonNullFields() {
        Categorie existing = buildCategorie(1L, "OriginalName");
        existing.setDescription("OriginalDesc");

        Categorie partial = new Categorie();
        partial.setNomCategorie("PatchedName");
        // description is null — should not be updated

        when(categorieRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(categorieRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Categorie result = categorieService.patchCategorie(1L, partial);

        assertThat(result.getNomCategorie()).isEqualTo("PatchedName");
        assertThat(result.getDescription()).isEqualTo("OriginalDesc");
    }

    @Test
    void patchCategorie_throwsNoSuchElement_whenNotFound() {
        when(categorieRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categorieService.patchCategorie(99L, new Categorie()))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void deleteCategorie_byId_callsRepository() {
        categorieService.deleteCategorie(1L);
        verify(categorieRepository).deleteById(1L);
    }

    @Test
    void deleteCategorie_byEntity_callsRepository() {
        Categorie cat = buildCategorie(1L, "ToDelete");
        categorieService.deleteCategorie(cat);
        verify(categorieRepository).delete(cat);
    }
}
