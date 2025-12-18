package com.amc.api.Controllers;


import com.amc.api.Entities.Category;
import com.amc.api.Repositories.CategoryRepository;
import com.amc.api.Services.CategoryService;
import com.amc.api.Utils.Exceptions;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/{uuid}")
    public ResponseEntity<Category> getCategoryByUuid(@PathVariable("uuid") String categoryUuid ) {
        if (categoryRepository.findByUuid(categoryUuid) == null)
            throw new Exceptions.ResourceNotFoundException("Categoria não encontrado.");
        Category category = categoryService.findCategoryByUuid(categoryUuid);
        return category != null ? ResponseEntity.ok(category) : ResponseEntity.badRequest().build();
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(categories);
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@Valid @RequestBody Category newCategory) {
        if (categoryRepository.findBySlug(newCategory.getSlug()) != null || categoryRepository.findByName(newCategory.getName()) != null)
            throw new Exceptions.DatabaseException("Já existe uma categoria com os dados informados.");
        Category category = categoryService.createCategory(newCategory);
        return category != null ? ResponseEntity.ok(category) : ResponseEntity.badRequest().build();
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Category> updateCategory(@Valid @PathVariable("uuid") String categoryUuid, @RequestBody Category categoryData) {
        if (categoryRepository.findByUuid(categoryUuid) == null)
            throw new Exceptions.ResourceNotFoundException("Categoria não encontrado.");
        Category category = categoryService.updateCategory(categoryUuid, categoryData);
        return category != null ? ResponseEntity.ok(category) : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Category> deleteCategory(@PathVariable("uuid") String categoryUuid ) {
        if (categoryRepository.findByUuid(categoryUuid) == null)
            throw new Exceptions.ResourceNotFoundException("Categoria não encontrada.");
        boolean category = categoryService.deleteCategory(categoryUuid);
        return category ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

}
