package com.example.shopapp.response;


import com.example.shopapp.Model.BaseModel;
import com.example.shopapp.Model.Product;
import com.example.shopapp.Model.ProductImage;
import com.example.shopapp.dto.ProductImageDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse extends BaseResponse {
    private Long id;
    private String name;
    private Float price;
    private String thumbnail;
    private String description;
    private Long categoryId;
    @JsonProperty("product_images")
    private List<ProductImageDTO> productImages = new ArrayList<>();

    public static ProductResponse convertResponse(Product product){
        ProductResponse productResponse =ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .thumbnail(product.getThumbnail())
                .description(product.getDescription())
                .categoryId(product.getCategory().getId())
                .productImages(
                        product.getProductImages().stream()
                                .map(pi -> ProductImageDTO.builder()
                                        .id(pi.getId())
                                        .imageUrl(pi.getImageUrl())
                                        .build())
                                .collect(Collectors.toList())
                )
                .build();
        productResponse.setCreatedAt(product.getCreatedAt());
        productResponse.setUpdatedAt(product.getUpdatedAt());
        return productResponse;
    }
}
