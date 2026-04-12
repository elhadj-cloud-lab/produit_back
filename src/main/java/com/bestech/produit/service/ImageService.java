package com.bestech.produit.service;

import com.bestech.produit.model.Image;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {

    Image uplaodImage(MultipartFile file) throws IOException;
    Image getImageDetails(Long id) throws IOException;
    ResponseEntity<byte[]> getImage(Long id) throws IOException;

    Image uplaodImageProd(MultipartFile file, Long idProd) throws IOException;
    List<Image> getImagesParProd(Long prodId);
    void deleteImage(Long id) ;
}
