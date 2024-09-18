package com.example.shopapp.controller;

import com.example.shopapp.Model.Category;
import com.example.shopapp.dto.CategoryDTO;
import com.example.shopapp.response.CategoryListResponse;
import com.example.shopapp.response.CategoryResponse;
import com.example.shopapp.response.OrderResponseNew;
import com.example.shopapp.response.UpdateCategoryResponse;
import com.example.shopapp.service.CategoryService;
import com.example.shopapp.components.LocalizationUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @GetMapping()
    public ResponseEntity<List<Category>> getAllCategory(){
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
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
    public ResponseEntity<UpdateCategoryResponse> updateCategory(@PathVariable long id, @Valid @ModelAttribute CategoryDTO categoryDTO){
        categoryService.updateCategory(id,categoryDTO);
        String message= localizationUtil.getLocalizedMessage("category.update_category.update_successfully");
        return ResponseEntity.ok(UpdateCategoryResponse.builder()
                .message(message)
                .build());
    }
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteCategory(@RequestParam long id){
        categoryService.deleteCategory(id);
        return  ResponseEntity.ok("delete category success");
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
