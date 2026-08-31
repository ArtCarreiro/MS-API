package com.amc.api.controllers;

import java.util.List;

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
        return ResponseEntity.ok(productBO.getAllProducts());
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Product> getProductByUuid(@PathVariable String uuid) {
        return ResponseEntity.ok(productBO.getProductByUuid(uuid));
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product newProductData) {
        return ResponseEntity.ok(productBO.createProduct(newProductData));
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable String productUuid, @Valid @RequestBody ProductDTO newProductData) {
        Product product = productRepository.findAll().stream()
                .filter(p -> p.getUuid().equals(productUuid) && p.getActive() && !p.getDeleted())
                .findFirst()
                .orElseThrow(() -> new Exceptions.ResourceNotFoundException("Produto", productUuid));
        return ResponseEntity.ok(productBO.updateProduct(newProductData, product)); 
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String uuid) {
        productBO.deleteProduct(uuid);
        return ResponseEntity.noContent().build();
    }
    
}