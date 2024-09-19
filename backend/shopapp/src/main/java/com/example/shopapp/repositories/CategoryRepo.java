package com.example.shopapp.repositories;

import com.example.shopapp.Model.Category;
import com.example.shopapp.Model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

public interface CategoryRepo extends JpaRepository<Category,Long> {
    @Query("SELECT c FROM Category c WHERE" +
            "(:keyword IS NULL OR :keyword = '' OR  c.name LIKE %:keyword%)")
    Page<Category> findByKeywordCategory(String keyword, Pageable pageable);
}
