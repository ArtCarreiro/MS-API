package com.amc.api.Services.Product;

import com.amc.api.Entities.Product.FileProduct;
import com.amc.api.Repositories.Product.FileProductRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileProductService {

    @Autowired
    private FileProductRepository fileProductRepository;

    @Autowired
    private ModelMapper modelMapper;

    public FileProduct findFileByUuid(String FileUuid) {
        try {
            return fileProductRepository.findByUuid(FileUuid);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public FileProduct updateFile(String FileUuid, FileProduct newFileProduct) {
        modelMapper.typeMap(FileProduct.class, FileProduct.class)
                .addMappings(mapper -> mapper.skip(FileProduct::setUuid));
        try {
            if (newFileProduct != null) {
                FileProduct newFileProductData = fileProductRepository.findByUuid(FileUuid);
                modelMapper.map(newFileProduct, newFileProductData);
                return fileProductRepository.save(newFileProductData);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Transactional
    public boolean deleteFile(String FileUuid) {
        try {
            FileProduct fileProductDeleted = fileProductRepository.findByUuid(FileUuid);
            fileProductRepository.delete(fileProductDeleted);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}
