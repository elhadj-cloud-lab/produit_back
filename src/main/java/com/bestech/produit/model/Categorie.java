package com.bestech.produit.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Categorie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCategorie;
    private String nomCategorie;
    private String description;

    @JsonIgnore
    @OneToMany(mappedBy = "categorie")
    private List<Produit> produits;

}
