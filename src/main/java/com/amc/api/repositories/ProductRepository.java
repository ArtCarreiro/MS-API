package com.amc.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.amc.api.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    
    Product findByName(String name);

    Product findByUuid(String uuid);

    void deleteProductByUuid(String uuid);

}
