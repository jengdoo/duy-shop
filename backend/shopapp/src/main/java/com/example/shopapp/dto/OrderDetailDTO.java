package com.example.shopapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailDTO {
    @JsonProperty("order_id")
    @Min(value = 1,message = "Order's Id must be > 0")
    private Long OrderId;
    @JsonProperty("product_id")
    @Min(value = 1,message = "Product's Id must be > 0")
    private Long ProductId;
    @Min(value = 0,message = "Price must be >=0")
    private Float price;
    @JsonProperty("number_of_products")
    @Min(value = 1,message = "Number of products must be >=1")
    private int numberOfProducts;
    @JsonProperty("total_money")
    @Min(value = 0,message = "Total money must be >=0")
    private Float totalMoney;
    private String color;
}
