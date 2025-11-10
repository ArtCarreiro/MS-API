package com.amc.api.Repositories.Product;

import com.amc.api.Entities.Product.FileProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileProductRepository extends JpaRepository<FileProduct, Long> {

    FileProduct findByUuid(String fileUuid);

}
