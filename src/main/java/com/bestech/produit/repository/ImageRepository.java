package com.bestech.produit.repository;

import com.bestech.produit.model.Image;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ImageRepository extends CrudRepository<Image,Long> {
    List<Image> findByProduitIdProduit(Long idProduit);
    void deleteByProduitIdProduit(Long idProduit);
}
