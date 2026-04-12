package com.bestech.produit.bean;

import com.bestech.bean.CategorieDTO;
import com.bestech.bean.ProduitDTO;
import com.bestech.produit.model.Categorie;
import com.bestech.produit.model.Produit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProduitBeanMapper {

    ProduitDTO toProduitDTO(Produit produit);


    @Mapping(target = "categorie.produits", ignore = true)
    @Mapping(target = "images", ignore = true)
    Produit toProduitEntity(ProduitDTO dto);

    List<ProduitDTO> toProduitDTO(List<Produit> produits);
    List<Produit> toProduitEntity(List<ProduitDTO> produitDTOS);

    CategorieDTO toCategorieDTO(Categorie categorie);

    @Mapping(target = "produits", ignore = true)
    Categorie toCategorieEntity(CategorieDTO dto);

    List<CategorieDTO> toCategorieDTO(List<Categorie> categorie);
    List<Categorie> toCategorieEntity(List<CategorieDTO> categorieDTOs);

}
