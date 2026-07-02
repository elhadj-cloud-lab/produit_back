package com.bestech.produit.controller;

import com.bestech.produit.model.Categorie;
import com.bestech.produit.repository.CategorieRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests d'intégration controller — sécurité désactivée (addFilters = false)
 * pour tester la logique controller indépendamment du filtre JWT.
 * La sécurité JWT est testée dans InMemoryTokenBlacklistServiceTest.
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
class CategorieControllerIntegrationTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired CategorieRepository categorieRepository;

    private Categorie savedCategorie(String nom) {
        Categorie c = new Categorie();
        c.setNomCategorie(nom);
        c.setDescription("Description de " + nom);
        return categorieRepository.save(c);
    }

    @Test
    void getAllCategories_returns200WithArray() throws Exception {
        savedCategorie("Électronique");
        savedCategorie("Informatique");

        mockMvc.perform(get("/api/categorie"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void createCategorie_savesAndReturnsDto() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of(
                "nomCategorie", "Smartphones",
                "description", "Téléphones mobiles"
        ));

        mockMvc.perform(post("/api/categorie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomCategorie").value("Smartphones"));
    }

    @Test
    void getCategorieById_returnsCorrectCategorie() throws Exception {
        Categorie cat = savedCategorie("Audio");

        mockMvc.perform(get("/api/categorie/" + cat.getIdCategorie()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomCategorie").value("Audio"))
                .andExpect(jsonPath("$.description").value("Description de Audio"));
    }

    @Test
    void updateCategorie_fullReplace() throws Exception {
        Categorie cat = savedCategorie("OldName");
        String body = objectMapper.writeValueAsString(Map.of(
                "nomCategorie", "NewName",
                "description", "NewDesc"
        ));

        mockMvc.perform(put("/api/categorie/" + cat.getIdCategorie())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomCategorie").value("NewName"));
    }

    @Test
    void patchCategorie_updatesOnlyProvidedFields() throws Exception {
        Categorie cat = savedCategorie("PatchMe");

        String patchBody = objectMapper.writeValueAsString(Map.of(
                "nomCategorie", "PatchedName"
        ));

        mockMvc.perform(patch("/api/categorie/" + cat.getIdCategorie())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomCategorie").value("PatchedName"))
                .andExpect(jsonPath("$.description").value("Description de PatchMe"));
    }

    @Test
    void deleteCategorie_returns204() throws Exception {
        Categorie cat = savedCategorie("ToDelete");

        mockMvc.perform(delete("/api/categorie/" + cat.getIdCategorie()))
                .andExpect(status().isNoContent());
    }
}
