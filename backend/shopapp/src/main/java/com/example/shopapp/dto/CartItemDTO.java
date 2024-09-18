package com.example.shopapp.dto;

import com.example.shopapp.Model.OrderDetail;
import com.example.shopapp.Model.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDTO {
    @JsonProperty("product_id")
    private Long productId;
    @JsonProperty("quantity")
    private int quantity;
    public OrderDetail toOrderDetail() {
        OrderDetail orderDetail = new OrderDetail();
        Product product = new Product();
        product.setId(this.productId);
        product.setQuantity(this.quantity);
        orderDetail.setProduct(product);
        return orderDetail;
    }
}
