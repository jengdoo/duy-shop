package com.example.shopapp.service;

import com.example.shopapp.Model.Order;
import com.example.shopapp.Model.Payment;
import com.example.shopapp.Model.PaymentStatus;
import com.example.shopapp.dto.PaymentDTO;
import com.example.shopapp.request.PaymentRequest;
import com.example.shopapp.response.OrderResponse;

public interface PaymentService {
    PaymentDTO createPayment( OrderResponse orderResponse) throws Exception;
    PaymentDTO getPaymentByTransactionId(String transactionId);
    void updatePaymentStatus(String transactionId, PaymentStatus status);
}
