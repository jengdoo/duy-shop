package com.example.shopapp.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryListResponse {
    private List<CategoryResponse> categoryResponses;
    private int page;
    private int pageSize;
    private int totalPage;
}
