package com.amc.api.Services;

import com.amc.api.Entities.Category;
import com.amc.api.Entities.Product;
import com.amc.api.Repositories.CategoryRepository;
import com.amc.api.Repositories.FileRepository;
import com.amc.api.Repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private ModelMapper modelMapper;

    public Product findProductByUuid(String ProductUuid) {
        try {
            return productRepository.findByUuid(ProductUuid);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public Product createProduct(Product newProduct) {
        try {
            Category category = categoryRepository.findByUuid(newProduct.getCategory().getUuid());
            newProduct.setCategory(category);
            fileRepository.saveAll(newProduct.getFiles());
            return productRepository.save(newProduct);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public Product updateProduct( String ProductUuid, Product newProduct) {
        modelMapper.typeMap(Product.class, Product.class)
                .addMappings(mapper -> mapper.skip(Product::setUuid));
        try {
            if (newProduct != null) {
                Product newProductData = productRepository.findByUuid(ProductUuid);
                modelMapper.map(newProduct, newProductData);
                return productRepository.save(newProductData);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Transactional
    public boolean deleteProduct(String ProductUuid) {
        try {
            Product ProductDeleted = productRepository.findByUuid(ProductUuid);
            productRepository.delete(ProductDeleted);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
