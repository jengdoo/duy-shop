package com.example.shopapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    @NotBlank(message = "product's name is required")
    @Size(min = 3,max = 200, message = "product's name must be between 3 and 200 characters")
    private String name;
    @Min(value = 0,message = "Price must be greater than or less to 0")
    @Max(value = 10000000,message = "Price must be less than or equals to 10,000,000")
    private Float price;
    private String thumbnail;
    private String description;
    @NotNull(message = "category id is required")
    @JsonProperty("category_id")
    private Long categoryId;
}
