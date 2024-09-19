package com.example.shopapp.response;

import com.example.shopapp.Model.Category;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponse {
    private  Long id;
    private String name;

    public static CategoryResponse convertCategoryResponse(Category category){
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
