package com.example.shopapp.response;

import com.example.shopapp.Model.Order;
import com.example.shopapp.Model.OrderStatus;
import com.example.shopapp.Model.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse{
    private Long id;
    @JsonProperty("user_id")
    private Long userId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
    private String note;
    private Date orderDate;
    private String status;
    private Float totalMoney;
    private String shippingMethod;
    private String shippingAddress;
    private LocalDate shippingDate;
    private String trackingNumber;
    private String paymentMethod;
    private Boolean active;

    public static OrderResponse convertRespo(Order order){
        return OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .fullName(order.getFullName())
                .email(order.getEmail())
                .phoneNumber(order.getPhoneNumber())
                .address(order.getAddress())
                .status(order.getStatus())
                .note(order.getNote())
                .orderDate(order.getOrderDate())
                .totalMoney(order.getTotalMoney())
                .shippingMethod(order.getShippingMethod())
                .paymentMethod(order.getPaymentMethod())
                .shippingDate(order.getShippingDate())
                .shippingAddress(order.getShippingAddress())
                .active(order.getActive())
                .build();
    }
}
