package com.example.shopapp.service;

import com.example.shopapp.Model.Category;
import com.example.shopapp.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    Category createCategory(CategoryDTO categoryDTO);
    Category getCategoryById(Long id);
    List<Category> getAllCategories();
    Category updateCategory(Long id,CategoryDTO categoryDTO);
    void deleteCategory(Long id);
}
