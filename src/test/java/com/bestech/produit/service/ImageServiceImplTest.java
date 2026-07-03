package com.bestech.produit.service;

import com.bestech.produit.model.Image;
import com.bestech.produit.model.Produit;
import com.bestech.produit.repository.ImageRepository;
import com.bestech.produit.repository.ProduitRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceImplTest {

    @Mock ImageRepository imageRepository;
    @Mock ProduitService produitService;
    @Mock ProduitRepository produitRepository;

    @InjectMocks ImageServiceImpl imageService;

    private Image buildImage(Long id, String name) {
        return Image.builder()
                .idImage(id)
                .name(name)
                .type("image/png")
                .image(new byte[]{1, 2, 3})
                .build();
    }

    @Test
    void uplaodImage_savesImageFromMultipartFile() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "file", "photo.png", "image/png", new byte[]{10, 20, 30});

        Image saved = buildImage(1L, "photo.png");
        when(imageRepository.save(any(Image.class))).thenReturn(saved);

        Image result = imageService.uplaodImage(file);

        assertThat(result.getName()).isEqualTo("photo.png");
        verify(imageRepository).save(any(Image.class));
    }

    @Test
    void getImageDetails_returnsImageBuiltFromDb() throws IOException {
        Image dbImage = buildImage(1L, "detail.png");
        when(imageRepository.findById(1L)).thenReturn(Optional.of(dbImage));

        Image result = imageService.getImageDetails(1L);

        assertThat(result.getIdImage()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("detail.png");
        assertThat(result.getImage()).containsExactly(1, 2, 3);
    }

    @Test
    void getImage_returnsResponseEntityWithCorrectMediaType() throws IOException {
        Image dbImage = buildImage(1L, "img.png");
        when(imageRepository.findById(1L)).thenReturn(Optional.of(dbImage));

        ResponseEntity<byte[]> response = imageService.getImage(1L);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getHeaders().getContentType()).hasToString("image/png");
        assertThat(response.getBody()).containsExactly(1, 2, 3);
    }

    @Test
    void deleteImage_callsRepositoryDeleteById() {
        imageService.deleteImage(5L);
        verify(imageRepository).deleteById(5L);
    }

    @Test
    void uplaodImageProd_linksImageToProduit() throws IOException {
        Produit produit = new Produit();
        produit.setIdProduit(10L);
        produit.setNomProduit("Laptop");

        MockMultipartFile file = new MockMultipartFile(
                "file", "laptop.jpg", "image/jpeg", new byte[]{5, 6});

        when(produitRepository.findById(10L)).thenReturn(Optional.of(produit));
        when(imageRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Image result = imageService.uplaodImageProd(file, 10L);

        assertThat(result.getProduit()).isSameAs(produit);
        assertThat(result.getName()).isEqualTo("laptop.jpg");
        assertThat(result.getType()).isEqualTo("image/jpeg");
    }

    @Test
    void uplaodImageProd_throwsRuntime_whenProduitNotFound() {
        MockMultipartFile file = new MockMultipartFile(
                "file", "img.png", "image/png", new byte[]{});

        when(produitRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> imageService.uplaodImageProd(file, 99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Produit introuvable");
    }

    @Test
    void getImagesParProd_returnsImagesForProduit() {
        List<Image> images = List.of(buildImage(1L, "img1.png"), buildImage(2L, "img2.png"));
        when(imageRepository.findByProduitIdProduit(10L)).thenReturn(images);

        List<Image> result = imageService.getImagesParProd(10L);

        assertThat(result).hasSize(2);
    }
}
