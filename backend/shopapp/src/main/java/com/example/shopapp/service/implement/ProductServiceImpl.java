package com.example.shopapp.service.implement;

import com.example.shopapp.Model.Category;
import com.example.shopapp.Model.Product;
import com.example.shopapp.Model.ProductImage;
import com.example.shopapp.dto.ProductDTO;
import com.example.shopapp.dto.ProductImageDTO;
import com.example.shopapp.exception.DataNotFoundException;
import com.example.shopapp.exception.InvalidParamException;
import com.example.shopapp.repositories.CategoryRepo;
import com.example.shopapp.repositories.ProductImageRepo;
import com.example.shopapp.repositories.ProductRepo;
import com.example.shopapp.response.ProductResponse;
import com.example.shopapp.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;
    private final ProductImageRepo productImageRepo;
    @Override
    @Transactional
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException {
       Category categoryExisted = categoryRepo.findById(productDTO.getCategoryId()).orElseThrow(()->new DataNotFoundException("Cannot find category with id"+productDTO.getCategoryId()));
        Product product =Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .thumbnail(productDTO.getThumbnail())
                .description(productDTO.getDescription())
                .category(categoryExisted)
                .build();
        return productRepo.save(product);
    }

    @Override
    public Product getProductById(Long id) throws DataNotFoundException {
        return productRepo.findById(id).orElseThrow(()->new DataNotFoundException("Cannot find product with id:"+id));
    }

    @Override
    public Page<ProductResponse> getAllProducts(PageRequest pageRequest) {
        return productRepo.findAll(pageRequest)
                .map(ProductResponse::convertResponse);
    }

    @Override
    @Transactional
    public Product updateProduct(Long id,ProductDTO productDTO) {
        Optional<Product> existedProduct = productRepo.findById(id);
        if(existedProduct.isPresent()){
            Category existedCategory = categoryRepo.findById(productDTO.getCategoryId()).orElseThrow(()->new RuntimeException("Cannot find category with id:"+productDTO.getCategoryId()));
            Product product = existedProduct.get();
            product.setName(productDTO.getName());
            product.setCategory(existedCategory);
            product.setPrice(productDTO.getPrice());
            product.setDescription(productDTO.getDescription());
            product.setThumbnail(productDTO.getThumbnail());
            return productRepo.save(product);
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Optional<Product> deleteProductById = productRepo.findById(id);
       deleteProductById.ifPresent(productRepo::delete);
    }

    @Override
    public boolean existsByName(String name) {
        return productRepo.existsByName(name);
    }
    @Override
    @Transactional
    public ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws Exception{
        Product existedProduct = productRepo.findById(productId).orElseThrow(()->new RuntimeException("Cannot find product with id:"+productId));
        ProductImage newProductImage = ProductImage.builder()
                .product(existedProduct)
                .imageUrl(productImageDTO.getImageUrl())
                .build();
        // not insert too 5 image for 1 product
        int size = productImageRepo.findByProductId(productId).size();
        if(size >=ProductImage.MAXIMUM_IMAGES_PER_PRODUCT){
            throw new InvalidParamException("Number of image must be less or equal 5");
        }
        return productImageRepo.save(newProductImage);
    }
}
