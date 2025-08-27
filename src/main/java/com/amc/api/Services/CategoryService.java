package com.amc.api.Services;

import com.amc.api.Entities.Category;
import com.amc.api.Repositories.CategoryRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository CategoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    public Category findCategoryByUuid(String CategoryUuid) {
        try {
            return CategoryRepository.findByUuid(CategoryUuid);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Category createCategory(Category newCategory) {
        try {
            return CategoryRepository.save(newCategory);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public Category updateCategory( String CategoryUuid, Category newCategory) {
        modelMapper.typeMap(Category.class, Category.class)
                .addMappings(mapper -> mapper.skip(Category::setUuid));
        try {
            if (newCategory != null) {
                Category newCategoryData = CategoryRepository.findByUuid(CategoryUuid);
                modelMapper.map(newCategory, newCategoryData);
                return CategoryRepository.save(newCategoryData);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Transactional
    public boolean deleteCategory(String CategoryUuid) {
        try {
            Category CategoryDeleted = CategoryRepository.findByUuid(CategoryUuid);
            CategoryRepository.delete(CategoryDeleted);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
