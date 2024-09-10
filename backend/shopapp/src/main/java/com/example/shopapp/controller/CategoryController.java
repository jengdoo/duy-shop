package com.example.shopapp.controller;

import com.example.shopapp.Model.Category;
import com.example.shopapp.dto.CategoryDTO;
import com.example.shopapp.dto.ProductDTO;
import com.example.shopapp.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    @GetMapping("list")
    public ResponseEntity<List<Category>> getAllCategory(){
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
    @PostMapping("/add")
    public ResponseEntity<?> addCategory(@Valid @ModelAttribute CategoryDTO categoryDTO, BindingResult result){
        if(result.hasErrors()){
            List<String> errorMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }
        categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok("add success");
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable long id,@Valid @ModelAttribute CategoryDTO categoryDTO){
        categoryService.updateCategory(id,categoryDTO);
        return ResponseEntity.ok("update category success");
    }
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteCategory(@RequestParam long id){
        categoryService.deleteCategory(id);
        return  ResponseEntity.ok("delete category success");
    }
}
