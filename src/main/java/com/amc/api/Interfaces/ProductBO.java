package com.amc.api.interfaces;

import com.amc.api.dto.ProductDTO;
import com.amc.api.entities.Product;

public interface ProductBO {
    
    Product createProduct(Product data);

    Product updateProduct(ProductDTO data, String uuid);

    boolean deleteProduct(String uuid);

    void validateProduct(Product product);

}
