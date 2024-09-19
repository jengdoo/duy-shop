package com.example.shopapp.response;

import com.example.shopapp.Model.Order;
import com.example.shopapp.Model.OrderDetail;
import com.example.shopapp.Model.Product;
import com.example.shopapp.dto.ProductDTO;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailResponse {
    private Long id;
    private Long orderId;
    private ProductDTO productId;
    private Float price;
    private int numberOfProducts;
    private Float totalMoney;
    private String color;

    public static OrderDetailResponse convertOrderDetailsResponse(OrderDetail orderDetail){
        return OrderDetailResponse.builder()
                .id(orderDetail.getId())
                .orderId(orderDetail.getOrder().getId())
                .productId(ProductDTO.convertProductDTO(orderDetail.getProduct()))
                .price(orderDetail.getPrice())
                .numberOfProducts(orderDetail.getNumberOfProducts())
                .totalMoney(orderDetail.getTotalMoney())
                .color(orderDetail.getColor())
                .build();
    }
}
