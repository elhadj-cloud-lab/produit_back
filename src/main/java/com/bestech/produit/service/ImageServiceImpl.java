package com.bestech.produit.service;

import com.bestech.produit.model.Image;
import com.bestech.produit.model.Produit;
import com.bestech.produit.repository.ImageRepository;
import com.bestech.produit.repository.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    ProduitService produitService;

    @Autowired
    ProduitRepository produitRepository;

    @Override
    public Image uplaodImage(MultipartFile file) throws IOException {
        return imageRepository.save(Image.builder().name(file.getOriginalFilename()).type(file.getContentType())
                .image(file.getBytes()).build());
    }

    @Override
    public Image getImageDetails(Long id) throws IOException {
        final Optional<Image> dbImage = imageRepository.findById(id);
        return Image.builder().idImage(dbImage.get().getIdImage()).name(dbImage.get().getName())
                .type(dbImage.get().getType()).image(dbImage.get().getImage()).build();
    }

    @Override
    public ResponseEntity<byte[]> getImage(Long id) throws IOException {
        final Optional<Image> dbImage = imageRepository.findById(id);
        return ResponseEntity.ok().contentType(MediaType.valueOf(dbImage.get().getType()))
                .body(dbImage.get().getImage());
    }

    @Override
    public void deleteImage(Long id) {
        imageRepository.deleteById(id);
    }

    @Override
    public Image uplaodImageProd(MultipartFile file,Long idProd)
            throws IOException {
        Produit produit = produitRepository.findById(idProd)
                .orElseThrow(() -> new RuntimeException("Produit introuvable"));
        Image image = Image.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .image(file.getBytes())
                .produit(produit)
                .build();
        return imageRepository.save(image);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Image> getImagesParProd(Long prodId) {
        return imageRepository.findByProduitIdProduit(prodId);
    }
}
