package com.bestech.produit.controller;

import com.bestech.produit.model.Categorie;
import com.bestech.produit.model.Produit;
import com.bestech.produit.repository.CategorieRepository;
import com.bestech.produit.repository.ProduitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests d'intégration ImageController — sécurité désactivée (addFilters = false).
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
class ImageControllerIntegrationTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired ProduitRepository produitRepository;
    @Autowired CategorieRepository categorieRepository;

    private Produit testProduit;

    @BeforeEach
    void setUp() {
        Categorie cat = new Categorie();
        cat.setNomCategorie("TestCat");
        cat.setDescription("desc");
        cat = categorieRepository.save(cat);

        Produit p = new Produit();
        p.setNomProduit("TestProduit");
        p.setPrixProduit(99.0);
        p.setCategorie(cat);
        testProduit = produitRepository.save(p);
    }

    // The controller uses @RequestParam("image") for all upload endpoints
    private MockMultipartFile pngFile() {
        return new MockMultipartFile("image", "test.png", "image/png", new byte[]{1, 2, 3, 4});
    }

    @Test
    void uploadImage_returns200WithImageMetadata() throws Exception {
        MvcResult result = mockMvc.perform(multipart("/api/image/upload")
                        .file(pngFile()))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertThat(body).contains("test.png");
    }

    @Test
    void uploadImageProd_linksImageToProduit() throws Exception {
        mockMvc.perform(multipart("/api/image/uploadImageProd/" + testProduit.getIdProduit())
                        .file(pngFile()))
                .andExpect(status().isOk());
    }

    @Test
    void getImagesProd_returnsListOfImages() throws Exception {
        // Upload an image first
        mockMvc.perform(multipart("/api/image/uploadImageProd/" + testProduit.getIdProduit())
                .file(pngFile()));

        mockMvc.perform(get("/api/image/getImagesProd/" + testProduit.getIdProduit()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getImageLoad_returnsImageBytes() throws Exception {
        MvcResult upload = mockMvc.perform(multipart("/api/image/upload")
                        .file(pngFile()))
                .andReturn();

        Long imageId = objectMapper.readTree(upload.getResponse().getContentAsString())
                .get("idImage").asLong();

        mockMvc.perform(get("/api/image/load/" + imageId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG));
    }

    @Test
    void getImageDetails_returnsMetadata() throws Exception {
        MvcResult upload = mockMvc.perform(multipart("/api/image/upload")
                        .file(pngFile()))
                .andReturn();

        Long imageId = objectMapper.readTree(upload.getResponse().getContentAsString())
                .get("idImage").asLong();

        mockMvc.perform(get("/api/image/get/info/" + imageId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("test.png"))
                .andExpect(jsonPath("$.type").value("image/png"));
    }

    @Test
    void deleteImage_returnsSuccessfully() throws Exception {
        MvcResult upload = mockMvc.perform(multipart("/api/image/upload")
                        .file(pngFile()))
                .andReturn();

        Long imageId = objectMapper.readTree(upload.getResponse().getContentAsString())
                .get("idImage").asLong();

        mockMvc.perform(delete("/api/image/delete/" + imageId))
                .andExpect(status().isOk());
    }

    @Test
    void updateImage_replacesImage() throws Exception {
        mockMvc.perform(multipart("/api/image/update")
                        .file(pngFile())
                        .with(request -> { request.setMethod("PUT"); return request; }))
                .andExpect(status().isOk());
    }
}
