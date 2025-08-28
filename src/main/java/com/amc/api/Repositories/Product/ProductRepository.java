package com.amc.api.Repositories.Product;

import com.amc.api.Entities.Product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findByUuid(String productUuid);
}
