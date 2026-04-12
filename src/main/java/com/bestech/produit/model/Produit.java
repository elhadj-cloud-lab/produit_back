package com.bestech.produit.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
@Entity
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProduit;
    private String nomProduit;
    private double prixProduit;
    private Date dateCreation;

    @ManyToOne
    private Categorie categorie;

    @OneToMany (mappedBy = "produit")
    private List<Image> images;
}
