package com.example.shopapp.service;

import com.example.shopapp.Model.Product;
import com.example.shopapp.Model.ProductImage;
import com.example.shopapp.dto.ProductDTO;
import com.example.shopapp.dto.ProductImageDTO;
import com.example.shopapp.exception.DataNotFoundException;
import com.example.shopapp.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    Product createProduct(ProductDTO productDTO) throws DataNotFoundException;
    Product getProductById(Long id) throws DataNotFoundException;
    Page<ProductResponse> getAllProducts(Long categoryId,String name,Pageable pageable);
    Product updateProduct(Long id,ProductDTO productDTO);
    void deleteProduct(Long id);
    boolean existsByName(String name);
     ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws Exception;
     List<ProductResponse> findByName(String name);
     List<ProductResponse> findByCategory(Long categoryId);
}
