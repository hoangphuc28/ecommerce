package com.example.ecommerce.service;

import com.example.ecommerce.model.Category;
import com.example.ecommerce.repo.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepo categoryRepo;
    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }
    public void addCategory(String name) {
        Category category = new Category();
        category.setName(name);
        categoryRepo.save(category);
    }
    public Category getCategory(Long id) {
       return categoryRepo.findById(id).orElse(null);
    }
    public void remove(Long id) {
        categoryRepo.deleteById(id);
    }
    public void updateCategory(Category newCategory) {
        categoryRepo.save(newCategory);
    }
}

