package com.example.shopapp.dto;

import com.example.shopapp.Model.Payment;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PaymentDTO {
    private Long id;
    private double amount;
    private String bankCode;
    private Long orderId;
    private String paymentUrl;
    public static PaymentDTO convertToDTO(Payment payment, String paymentUrl) {
        return PaymentDTO.builder()
                .id(payment.getId())
                .amount(payment.getAmount())
                .orderId(payment.getOrder().getId())
                .paymentUrl(paymentUrl)
                .build();
    }
}
