package com.amc.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.amc.api.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    
    Product findByName(String name);

    Product findByUuid(String uuid);

    @Modifying
    @Query(
        value = """
            UPDATE products 
            SET deleted = true, active = false
            WHERE uuid = :uuid
        """, nativeQuery = true)
    void deleteProductByUuid(@Param("uuid") String uuid);

}
