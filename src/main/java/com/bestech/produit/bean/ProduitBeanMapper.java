package com.bestech.produit.bean;

import __package.projet_.bean.ProduitDTO;
import com.bestech.produit.model.Produit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProduitBeanMapper {

    ProduitDTO toDTO(Produit produit);
    @Mapping(target = "categorie.produits", ignore = true)
    Produit toEntity(ProduitDTO dto);

    List<ProduitDTO> toDTO(List<Produit> produits);
    List<Produit> toEntity(List<ProduitDTO> produitDTOS);

}
