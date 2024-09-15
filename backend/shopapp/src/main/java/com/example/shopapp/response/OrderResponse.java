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
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse{
    private Long id;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("fullname")
    private String fullName;
    private String email;
    @JsonProperty("phone_number")
    private String phoneNumber;
    private String address;
    private String note;
    @JsonProperty("order_date")
    private Date orderDate;
    private String status;
    @JsonProperty("total_money")
    private Float totalMoney;
    @JsonProperty("shipping_method")
    private String shippingMethod;
    @JsonProperty("shipping_address")
    private String shippingAddress;
    @JsonProperty("shipping_date")
    private LocalDate shippingDate;
    @JsonProperty("tracking_number")
    private String trackingNumber;
    @JsonProperty("payment_method")
    private String paymentMethod;
    @JsonProperty("order_details")
    private List<OrderDetailResponse> orderDetailResponses;

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
                .orderDetailResponses(order.getOrderDetails().stream().map(OrderDetailResponse::convertOrderDetailsResponse).toList())
                .build();
    }
}
