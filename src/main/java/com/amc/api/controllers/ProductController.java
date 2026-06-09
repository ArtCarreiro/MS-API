package com.amc.api.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amc.api.dto.response.ProductDTO;
import com.amc.api.entities.Product;
import com.amc.api.interfaces.ProductBO;
import com.amc.api.repositories.ProductRepository;
import com.amc.api.utils.Exceptions;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductBO productBO;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productRepository.findAll().stream()
                                .filter(product -> product.getActive() && !product.getDeleted())
                                .collect(Collectors.toList());
        return products != null ? ResponseEntity.ok(products) : ResponseEntity.noContent().build();
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Product> getProductByUuid(@PathVariable String uuid) {
        return productRepository.findAll().stream()
                .filter(product -> product.getUuid().equals(uuid))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());      
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product data) {
        Product product = productBO.createProduct(data);
        return product != null ? ResponseEntity.ok(product) : ResponseEntity.badRequest().build();
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Product> updateProduct(@PathVariable String uuid, @Valid @RequestBody ProductDTO data) {
        Product product =  productBO.updateProduct(data, uuid);
        return product != null ? ResponseEntity.ok(product) : ResponseEntity.badRequest().build(); 
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String uuid) {
        Product product = productRepository.findAll().stream()
                .filter(p -> p.getUuid().equals(uuid) && p.getActive() && !p.getDeleted())
                .findFirst()
                .orElseThrow(() -> new Exceptions.ResourceNotFoundException("Produto", uuid));
        return productBO.deleteProduct(product.getUuid()) == true ? ResponseEntity.noContent().build() : ResponseEntity.badRequest().build();
    }
    
}