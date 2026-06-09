package com.amc.api.services;

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
    @Transactional
    public Product createProduct(Product data) {
        validateProduct(data);
        return productRepository.save(data);  
    }

    @Override
    @Transactional
    public Product updateProduct(ProductDTO data, String uuid) {
        Product product = productRepository.findByUuid(uuid);
        validateProduct(product);
        try {
            mapper.map(data, product);
            return productRepository.save(product);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public boolean deleteProduct(String uuid) {
        try {
            productRepository.deleteProductByUuid(uuid);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void validateProduct(Product product) {
        if (productRepository.findByUuid(product.getUuid()) == null) 
             throw new Exceptions.ResourceNotFoundException("Produto não encontrado");
        if (productRepository.findByName(product.getName()) != null) 
            throw new Exceptions.DatabaseException("Produto já existe");
    }
}