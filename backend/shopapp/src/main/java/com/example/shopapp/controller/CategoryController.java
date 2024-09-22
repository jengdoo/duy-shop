package com.example.shopapp.controller;

import com.example.shopapp.Model.Category;
import com.example.shopapp.dto.CategoryDTO;
import com.example.shopapp.response.CategoryListResponse;
import com.example.shopapp.response.CategoryResponse;
import com.example.shopapp.response.OrderResponseNew;
import com.example.shopapp.response.UpdateCategoryResponse;
import com.example.shopapp.service.CategoryService;
import com.example.shopapp.components.LocalizationUtil;
import com.example.shopapp.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final LocalizationUtil localizationUtil;

    @GetMapping("")
    public ResponseEntity<List<Category>> getAllCategory(){
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
    @GetMapping("/finById")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getCategoryById(@RequestParam Long id){
        try {
            Category category = categoryService.getCategoryById(id);
            return ResponseEntity.ok(CategoryResponse.convertCategoryResponse(category));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Không thấy danh mục với id"+id);
        }
    }
    @PostMapping("/add")
    public ResponseEntity<?> addCategory(@Valid @RequestBody CategoryDTO categoryDTO, BindingResult result){
        if(result.hasErrors()){
            List<String> errorMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }
        Category category = categoryService.createCategory(categoryDTO);
        Map<String, Object> response = new HashMap<>();
        response.put("product", category);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable long id, @Valid @RequestBody CategoryDTO categoryDTO,BindingResult result){
        if(result.hasErrors()){
            List<String> message = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(message);
        }
        Category category = categoryService.updateCategory(id,categoryDTO);
        Map<String,Object> response = new HashMap<>();
        response.put("Thông báo",localizationUtil.getLocalizedMessage(MessageKeys.UPDATE_CATEGORY_SUCCESSFULLY));
        response.put("category",category);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteCategory(@RequestParam long id){
        categoryService.deleteCategory(id);
        Map<String,Object> response = new HashMap<>();
        response.put("Thông báo",localizationUtil.getLocalizedMessage(MessageKeys.DELETE_CATEGORY_SUCCESSFULLY));
        response.put("category with id:",id);
        return  ResponseEntity.ok(response);
    }
    @GetMapping("/get-categories-by-keyword")
    public ResponseEntity<?> getCategoryByKeyword(@RequestParam(defaultValue = "",required = false) String keyword,
                                                  @RequestParam(defaultValue = "0",required = false) int page,
                                                  @RequestParam(defaultValue = "10",required = false) int limit){
        try {
            Pageable pageable = PageRequest.of(page,limit, Sort.by("id"));
            Page<CategoryResponse> categoryResponsePage = categoryService.pageCategory(keyword,pageable);
            List<CategoryResponse> categoryResponses = categoryResponsePage.getContent();
            int totalPage = categoryResponsePage.getTotalPages();
            int pageCurrent = categoryResponsePage.getNumber();
            int pageSizeCurrent = categoryResponsePage.getSize();
            return ResponseEntity.ok(CategoryListResponse.builder()
                    .categoryResponses(categoryResponses)
                    .page(pageCurrent)
                    .pageSize(pageSizeCurrent)
                    .totalPage(totalPage)
                    .build());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
