package com.bestech.produit.controller;

import com.bestech.produit.model.Categorie;
import com.bestech.produit.model.Produit;
import com.bestech.produit.repository.CategorieRepository;
import com.bestech.produit.repository.ProduitRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests d'intégration controller — sécurité désactivée (addFilters = false)
 * pour tester la logique controller indépendamment du filtre JWT.
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
class ProduitControllerIntegrationTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired ProduitRepository produitRepository;
    @Autowired CategorieRepository categorieRepository;

    private Categorie testCategorie;

    @BeforeEach
    void setUp() {
        testCategorie = new Categorie();
        testCategorie.setNomCategorie("TestCategory");
        testCategorie.setDescription("Category for tests");
        testCategorie = categorieRepository.save(testCategorie);
    }

    private Produit savedProduit(String nom, double prix) {
        Produit p = new Produit();
        p.setNomProduit(nom);
        p.setPrixProduit(prix);
        p.setCategorie(testCategorie);
        return produitRepository.save(p);
    }

    private ObjectNode produitBody(String nom, double prix) {
        ObjectNode body = objectMapper.createObjectNode();
        body.put("nomProduit", nom);
        body.put("prixProduit", prix);
        body.putNull("dateCreation");
        ObjectNode cat = body.putObject("categorie");
        cat.put("idCategorie", testCategorie.getIdCategorie());
        return body;
    }

    @Test
    void getAllProduits_returns200WithArray() throws Exception {
        savedProduit("iPhone 15", 999.0);
        savedProduit("MacBook Pro", 1999.0);

        mockMvc.perform(get("/api/produit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void createProduit_returns200WithSavedProduit() throws Exception {
        mockMvc.perform(post("/api/produit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(produitBody("Galaxy S24", 849.0).toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomProduit").value("Galaxy S24"))
                .andExpect(jsonPath("$.prixProduit").value(849.0));
    }

    @Test
    void getProduitById_returnsCorrectProduit() throws Exception {
        Produit saved = savedProduit("Surface Pro", 1200.0);

        mockMvc.perform(get("/api/produit/" + saved.getIdProduit()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomProduit").value("Surface Pro"));
    }

    @Test
    void updateProduit_fullReplace() throws Exception {
        Produit saved = savedProduit("OldProduit", 100.0);

        ObjectNode update = produitBody("UpdatedProduit", 150.0);
        mockMvc.perform(put("/api/produit/" + saved.getIdProduit())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(update.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomProduit").value("UpdatedProduit"));
    }

    @Test
    void patchProduit_partialUpdate() throws Exception {
        Produit saved = savedProduit("PatchProduit", 100.0);

        ObjectNode patch = objectMapper.createObjectNode();
        patch.put("nomProduit", "PatchedProduit");
        patch.put("prixProduit", 110.0);

        mockMvc.perform(patch("/api/produit/" + saved.getIdProduit())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patch.toString()))
                .andExpect(status().isOk());
    }

    @Test
    void deleteProduit_returns200() throws Exception {
        Produit saved = savedProduit("ToDelete", 50.0);

        mockMvc.perform(delete("/api/produit/" + saved.getIdProduit()))
                .andExpect(status().isOk());
    }

    @Test
    void searchByNomExact_returnsMatchingProduits() throws Exception {
        savedProduit("Samsung S24", 850.0);

        mockMvc.perform(get("/api/produit/search/by-nom")
                        .param("nom", "Samsung S24"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void searchByNomContains_returnsMatchingProduits() throws Exception {
        savedProduit("Apple Watch Ultra", 799.0);

        mockMvc.perform(get("/api/produit/search/by-nom-contains")
                        .param("nom", "Apple"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void searchByNomAndMinPrix_returnsFilteredProduits() throws Exception {
        mockMvc.perform(get("/api/produit/search/by-nom-prix")
                        .param("nom", "Mac")
                        .param("prix", "500"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void sortByNom_returnsSortedList() throws Exception {
        savedProduit("Zara Watch", 200.0);
        savedProduit("Alpha Headphones", 150.0);

        mockMvc.perform(get("/api/produit/sort/by-nom"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void sortByNomAndPrix_returnsSortedList() throws Exception {
        mockMvc.perform(get("/api/produit/sort/by-nom-prix"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void searchByCategorieId_returnsProduitsForCategory() throws Exception {
        savedProduit("Prod In Cat", 99.0);

        mockMvc.perform(get("/api/produit/search/by-categorie-id")
                        .param("idCategorie", testCategorie.getIdCategorie().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
