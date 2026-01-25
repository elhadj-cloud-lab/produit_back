package com.bestech.produit.controller;

import __package.projet_.bean.Produit;
import __package.projet_.controller.ProduitApi;
import com.bestech.produit.service.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ProduitController implements ProduitApi {

    @Autowired
    private ProduitService produitService;

    @Override
    public ResponseEntity<List<Produit>> getAllProduits() {
        List<Produit> clients = produitService.getAllProduits();
        return ResponseEntity.ok(clients);
    }
}
