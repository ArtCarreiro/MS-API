package com.amc.api.Repositories.Product;

import com.amc.api.Entities.Product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    Product findByUuid(String productUuid);

    Product findBySlug(String slug);

    List<Product> findAllProductsByCategorySlug(String categorySlug);
}
