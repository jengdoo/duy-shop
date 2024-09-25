package com.example.shopapp.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class PaymentRequest {
    @JsonProperty("order_id")
    private Long orderId;
    private String paymentMethod;
}
