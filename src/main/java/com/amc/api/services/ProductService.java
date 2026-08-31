package com.amc.api.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amc.api.dto.response.ProductDTO;
import com.amc.api.entities.Product;
import com.amc.api.interfaces.ProductBO;
import com.amc.api.repositories.ProductRepository;
import com.amc.api.utils.Exceptions;

import jakarta.transaction.Transactional;

@Service
public class ProductService implements ProductBO {
    
    
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll().stream()
                .filter(product -> product.getActive() && !product.getDeleted())
                .collect(Collectors.toList());
    }

    @Override
    public Product getProductByUuid(String uuid) {
        return productRepository.findAll().stream()
                .filter(p -> p.getUuid().equals(uuid) && p.getActive() && !p.getDeleted())
                .findFirst()
                .orElseThrow(() -> new Exceptions.ResourceNotFoundException("Produto", uuid));
    }

    @Override
    @Transactional
    public Product createProduct(Product newProductData) {
        try {
            return productRepository.saveAndFlush(newProductData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(ProductDTO newProductData, Product product) {
        try {
            mapper.map(newProductData, product);
            productRepository.save(product);
            return newProductData;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public void deleteProduct(String uuid) {
        try {
            productRepository.deleteProductByUuid(uuid);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}