package com.amc.api.Controllers.Product;

import com.amc.api.Entities.Product.Product;
import com.amc.api.Repositories.Product.ProductRepository;
import com.amc.api.Services.Product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(products);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Product> getProductByUuid(@PathVariable("uuid") String ProductUuid ) {
        Product Product = productService.findProductByUuid(ProductUuid);
        return Product != null ? ResponseEntity.ok(Product) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{slug}")
    public ResponseEntity<Product> getProductBySlug(@PathVariable("slug") String slug) {
        Product product = productService.findProductBySlug(slug);
        return product != null ? ResponseEntity.ok().body(product) : ResponseEntity.badRequest().build();
    }

    @GetMapping("/category/{categorySlug}")
    public ResponseEntity<Product> getProductsByCategorySlug(@PathVariable("categorySlug") String categorySlug) {
        Product product = productService.findProductsByCategorySlug(categorySlug);
        return product != null ? ResponseEntity.ok().body(product) : ResponseEntity.badRequest().build();
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product newUser) {
        Product Product = productService.createProduct(newUser);
        return Product != null ? ResponseEntity.ok(Product) : ResponseEntity.badRequest().build();
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Product> updateProduct(@PathVariable("uuid") String ProductUuid, @RequestBody Product ProductData) {
        Product Product = productService.updateProduct(ProductUuid, ProductData);
        return Product != null ? ResponseEntity.ok(Product) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Product> deleteProduct(@PathVariable("uuid") String ProductUuid ) {
        boolean Product = productService.deleteProduct(ProductUuid);
        return Product ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

}
