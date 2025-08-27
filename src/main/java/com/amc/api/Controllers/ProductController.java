package com.amc.api.Controllers;

import com.amc.api.Entities.Product;
import com.amc.api.Repositories.ProductRepository;
import com.amc.api.Services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService ProductService;

    @Autowired
    private ProductRepository ProductRepository;

    @GetMapping("/{uuid}")
    public ResponseEntity<Product> getProductByUuid(@PathVariable("uuid") String ProductUuid ) {
        Product Product = ProductService.findProductByUuid(ProductUuid);
        return Product != null ? ResponseEntity.ok(Product) : ResponseEntity.notFound().build();
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Product>> getAllCategories() {
        List<Product> categories = ProductRepository.findAll();
        return categories.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(categories);
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product newUser) {
        Product Product = ProductService.createProduct(newUser);
        return Product != null ? ResponseEntity.ok(Product) : ResponseEntity.badRequest().build();
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Product> updateProduct(@PathVariable("uuid") String ProductUuid, @RequestBody Product ProductData) {
        Product Product = ProductService.updateProduct(ProductUuid, ProductData);
        return Product != null ? ResponseEntity.ok(Product) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Product> deleteProduct(@PathVariable("uuid") String ProductUuid ) {
        boolean Product = ProductService.deleteProduct(ProductUuid);
        return Product ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

}
