package com.example.shopapp.repositories;

import com.example.shopapp.Model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product,Long> {
    boolean existsByName(String name);

    @Query("SELECT p FROM Product p WHERE " +
            "(:name IS NULL OR :name = '' OR p.name LIKE %:name% OR p.description LIKE %:name%)" +
            "AND (:categoryId IS NULL OR :categoryId = 0 OR p.category.id = :categoryId) ")
    Page<Product> searchProducts(@Param("categoryId") Long categoryId,
                                 @Param("name") String name,
                                 Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %:name%")
    List<Product> findByNameContaining(@Param("name") String name);

    List<Product> findByCategoryId(Long categoryId);

    @Query("SELECT p FROM Product p WHERE p.id IN :productIds")
    List<Product> findByProductById(@Param("productIds") List<Long> productIds);
}
