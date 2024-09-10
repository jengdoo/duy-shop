package com.example.shopapp.repositories;

import com.example.shopapp.Model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product,Long> {
    boolean existsByName(String name);

    Page<Product> findAll(Pageable pageable);
}
