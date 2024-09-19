package com.example.shopapp.service;

import com.example.shopapp.Model.Order;
import com.example.shopapp.dto.OrderDTO;
import com.example.shopapp.exception.DataNotFoundException;
import com.example.shopapp.response.OrderResponse;
import com.example.shopapp.response.OrderResponseNew;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderDTO orderDTO) throws DataNotFoundException, Exception;
    OrderResponse getOrder(Long id);
    OrderResponse updateOrder(Long id,OrderDTO orderDTO);
    void deleteOrder(Long id);
    List<OrderResponse> getAllOrdersByUserId(Long userId);
    Page<OrderResponseNew> getOrdersByKeyword(String keyword, Pageable pageable);
    OrderResponse updateStatus(Long id,String status) throws Exception;
}
