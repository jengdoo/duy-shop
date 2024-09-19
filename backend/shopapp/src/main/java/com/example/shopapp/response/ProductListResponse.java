package com.example.shopapp.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductListResponse {
    private List<ProductResponse> productResponseList;
    private int page;
    private int pageSize;
    private int totalPage;
}
