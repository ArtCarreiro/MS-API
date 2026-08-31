package com.amc.api.interfaces;

import java.util.List;

import com.amc.api.dto.response.ProductDTO;
import com.amc.api.entities.Product;

public interface ProductBO {

    Product getProductByUuid(String productUuid);

    List<Product> getAllProducts();
    
    Product createProduct(Product newProductData);

    ProductDTO updateProduct(ProductDTO newProductData, Product product);

    void deleteProduct(String productUuid);

}
