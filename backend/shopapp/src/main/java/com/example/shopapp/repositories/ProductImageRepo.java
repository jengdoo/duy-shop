package com.example.shopapp.repositories;

import com.example.shopapp.Model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepo extends JpaRepository<ProductImage,Long> {
    List<ProductImage> findByProductId(Long id);
}
