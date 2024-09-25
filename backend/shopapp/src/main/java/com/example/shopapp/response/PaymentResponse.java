package com.example.shopapp.response;

import com.example.shopapp.Model.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponse {
    private PaymentStatus status;
    private String message;
}
