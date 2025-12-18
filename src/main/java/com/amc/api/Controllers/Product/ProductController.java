package com.amc.api.Controllers.Product;

import com.amc.api.Entities.Product.Product;
import com.amc.api.Repositories.CategoryRepository;
import com.amc.api.Repositories.Product.ProductRepository;
import com.amc.api.Services.Product.ProductService;
import com.amc.api.Utils.Exceptions;
import jakarta.validation.Valid;
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

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/{uuid}")
    public ResponseEntity<Product> getProductByUuid(@PathVariable("uuid") String productUuid ) {
        if (productRepository.findByUuid(productUuid) == null)
            throw new Exceptions.ResourceNotFoundException("Produto não encontrado.");
        Product product = productService.findProductByUuid(productUuid);
        return product != null ? ResponseEntity.ok(product) : ResponseEntity.badRequest().build();
    }

    @GetMapping("/{slug}")
    public ResponseEntity<Product> getProductBySlug(@PathVariable("slug") String productSlug) {
        if (productRepository.findBySlug(productSlug) == null)
            throw new Exceptions.ResourceNotFoundException("Produto não encontrado.");
        Product product = productService.findProductBySlug(productSlug);
        return product != null ? ResponseEntity.ok().body(product) : ResponseEntity.badRequest().build();
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(products);
    }

    @GetMapping("/category/{categorySlug}")
    public ResponseEntity<List<Product>> getAllProductsByCategorySlug(@PathVariable("categorySlug") String categorySlug) {
        if (categoryRepository.findBySlug(categorySlug) == null)
            throw new Exceptions.ResourceNotFoundException("Categoria não encontrada.");
        List<Product> product = productService.findAllProductsByCategorySlug(categorySlug);
        return product != null ? ResponseEntity.ok().body(product) : ResponseEntity.badRequest().build();
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product newProduct) {
        if (productRepository.findBySlug(newProduct.getSlug()) != null)
            throw new Exceptions.DatabaseException("Já existe um produto com os dados informados.");
        Product product = productService.createProduct(newProduct);
        return product != null ? ResponseEntity.ok(product) : ResponseEntity.badRequest().build();
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Product> updateProduct(@Valid @PathVariable("uuid") String productUuid, @RequestBody Product productData) {
        if (productRepository.findByUuid(productUuid) == null)
            throw new Exceptions.ResourceNotFoundException("Produto não encontrado.");
        Product product = productService.updateProduct(productUuid, productData);
        return product != null ? ResponseEntity.ok(product) : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> deleteProduct(@PathVariable("uuid") String productUuid ) {
        if (productRepository.findByUuid(productUuid) == null)
            throw new Exceptions.ResourceNotFoundException("Produto não encontrado.");
        boolean product = productService.deleteProduct(productUuid);
        return product ? ResponseEntity.noContent().build() : ResponseEntity.badRequest().build();
    }

}
