package com.example.shopapp.dto;

import com.example.shopapp.Model.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ProductImageDTO {
    @JsonProperty("product_id")
    @Min(value = 1,message = "Product's id must be than 1")
    private Long productId;
    @JsonProperty("image_url")
    @Size(min = 5,max = 200,message = "Image's name must be between 5 and 200 character")
    private String imageUrl;
}
