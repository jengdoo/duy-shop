package com.example.shopapp.repositories;

import com.example.shopapp.Model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepo extends JpaRepository<Order,Long> {
    List<Order> findByUserId(Long userId);

    @Query("SELECT o FROM Order o WHERE " +
            "(:keyword IS NULL OR :keyword = '' OR o.fullName like %:keyword% OR o.address LIKE %:keyword%)")
    Page<Order> findByKeyword(String keyword, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.txnRef = :txnRef")
    Order findByTxnRef(String txnRef);
}
