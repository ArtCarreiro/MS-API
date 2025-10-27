package com.amc.api.Controllers;


import com.amc.api.Entities.Category;
import com.amc.api.Repositories.CategoryRepository;
import com.amc.api.Services.CategoryService;
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
        Category category = categoryService.findCategoryByUuid(categoryUuid);
        return category != null ? ResponseEntity.ok(category) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(categories);
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category newUser) {
        Category category = categoryService.createCategory(newUser);
        return category != null ? ResponseEntity.ok(category) : ResponseEntity.badRequest().build();
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Category> updateCategory(@PathVariable("uuid") String categoryUuid, @RequestBody Category categoryData) {
        Category category = categoryService.updateCategory(categoryUuid, categoryData);
        return category != null ? ResponseEntity.ok(category) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Category> deleteCategory(@PathVariable("uuid") String categoryUuid ) {
        boolean category = categoryService.deleteCategory(categoryUuid);
        return category ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

}
