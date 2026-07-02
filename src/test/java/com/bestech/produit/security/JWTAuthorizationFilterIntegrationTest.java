package com.bestech.produit.security;

import com.bestech.produit.util.TestJwtHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests d'intégration du filtre JWT — avec la chaîne de sécurité complète.
 * Vérifie que le filtre traite correctement les tokens valides, expirés, invalides.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class JWTAuthorizationFilterIntegrationTest {

    @Autowired MockMvc mockMvc;

    @Test
    void requestWithExpiredToken_returns401() throws Exception {
        mockMvc.perform(get("/api/produit")
                        .header("Authorization", "Bearer " + TestJwtHelper.expiredToken()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void requestWithInvalidToken_returns401() throws Exception {
        mockMvc.perform(get("/api/produit")
                        .header("Authorization", "Bearer not.a.valid.jwt.at.all"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void requestWithMalformedHeader_passesFilter_butFailsSecurity() throws Exception {
        // Not "Bearer " prefix → filter ignores it → Spring Security default applies
        mockMvc.perform(get("/api/produit")
                        .header("Authorization", "Token " + TestJwtHelper.userToken()))
                .andExpect(result ->
                        org.assertj.core.api.Assertions.assertThat(
                                result.getResponse().getStatus()
                        ).isIn(401, 403));
    }

    @Test
    void requestWithoutToken_returnsSecurityError() throws Exception {
        // No token → Spring Security default behavior (401 or 403 depending on entry point)
        mockMvc.perform(get("/api/produit"))
                .andExpect(result ->
                        org.assertj.core.api.Assertions.assertThat(
                                result.getResponse().getStatus()
                        ).isIn(401, 403));
    }
}
