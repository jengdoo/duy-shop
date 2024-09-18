package com.example.shopapp.response;

import com.example.shopapp.Model.Order;
import com.example.shopapp.Model.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseNew {
    private Long id;
    @JsonProperty("fullname")
    private String fullName;
    @JsonProperty("user_id")
    private long userId;
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
    @JsonProperty("active")
    private boolean active;
    @JsonProperty("cart_items")
    private List<OrderDetailResponse> orderDetailResponses;

    public static OrderResponseNew convertResponseNew(Order order){
        User user = order.getUser();

        // Nếu user là null, bạn có thể chọn cách xử lý khác, ví dụ như đặt userId là -1 hoặc để trống
        Long userId = (user != null) ? user.getId() : 1;
        return OrderResponseNew.builder()
                .id(order.getId())
                .fullName(order.getFullName())
                .userId(userId)
                .email(order.getEmail())
                .phoneNumber(order.getPhoneNumber())
                .address(order.getAddress())
                .status(order.getStatus())
                .note(order.getNote())
                .active(order.getActive())
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
