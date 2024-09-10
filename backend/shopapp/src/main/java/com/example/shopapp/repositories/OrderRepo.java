package com.example.shopapp.repositories;

import com.example.shopapp.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order,Long> {
    List<Order> findByUserId(Long userId);
}
