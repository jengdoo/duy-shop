package com.example.shopapp.service.implement;

import com.example.shopapp.Model.Category;
import com.example.shopapp.dto.CategoryDTO;
import com.example.shopapp.repositories.CategoryRepo;
import com.example.shopapp.response.CategoryResponse;
import com.example.shopapp.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepo categoryRepo;
    @Override
    public Category createCategory(CategoryDTO categoryDTO) {
        Category category = Category.builder().name(categoryDTO.getName()).build();
        return categoryRepo.save(category);
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepo.findById(id).orElseThrow(()-> new RuntimeException("Category not found"));
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }

    @Override
    public Category updateCategory(Long id, CategoryDTO categoryDTO) {
        Category ct = categoryRepo.findById(id).get();
        ct.setName(categoryDTO.getName());
        categoryRepo.save(ct);
        return ct;
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepo.deleteById(id);
    }

    @Override
    public Category findById(Long id) {
        return categoryRepo.findById(id).orElseThrow(()->new RuntimeException("Không tìm thấy danh mục với id:"+id));
    }

    @Override
    public Page<CategoryResponse> pageCategory(String keyword, Pageable pageable) {
        Page<Category> categoryPage =categoryRepo.findByKeywordCategory(keyword,pageable);
        return categoryPage.map(CategoryResponse::convertCategoryResponse);
    }
}
