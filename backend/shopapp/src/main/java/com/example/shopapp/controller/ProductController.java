package com.example.shopapp.controller;

import ch.qos.logback.core.util.StringUtil;
import com.example.shopapp.Model.Product;
import com.example.shopapp.Model.ProductImage;
import com.example.shopapp.components.LocalizationUtil;
import com.example.shopapp.dto.CategoryDTO;
import com.example.shopapp.dto.ProductDTO;
import com.example.shopapp.dto.ProductImageDTO;
import com.example.shopapp.exception.DataNotFoundException;
import com.example.shopapp.response.ProductListResponse;
import com.example.shopapp.response.ProductResponse;
import com.example.shopapp.service.ProductService;
import com.example.shopapp.utils.MessageKeys;
import com.github.javafaker.Faker;
import jakarta.validation.Path;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/product")
public class ProductController {
    private final ProductService productService;
    private final LocalizationUtil localizationUtil;

    @GetMapping("")
    public ResponseEntity<?> getProduct(@RequestParam(defaultValue = "") String name,
                                                          @RequestParam(defaultValue = "", value = "category_id") Long categoryId,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("id").ascending());
        Page<ProductResponse> productPage = productService.getAllProducts(categoryId, name, pageable);

        int totalPage = productPage.getTotalPages();
        int pageCurrent = productPage.getNumber();
        int pageSizeCurrent = productPage.getSize();
        List<ProductResponse> products = productPage.getContent();

        return ResponseEntity.ok(ProductListResponse.builder()
                .productResponseList(products)
                .page(pageCurrent)
                .pageSize(pageSizeCurrent)
                .totalPage(totalPage)
                .build());
    }
    @PostMapping(value = "/add")
    public ResponseEntity<?> addCategory(@Valid @RequestBody ProductDTO productDTO,
                                         BindingResult result){
        try {
            if(result.hasErrors()){
                List<String> errorMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            Product newProduct =productService.createProduct(productDTO);
            return ResponseEntity.ok(localizationUtil.getLocalizedMessage(MessageKeys.INSERT_PRODUCT_SUCCESSFULLY,newProduct));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping(value = "uploads/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(@PathVariable("id") Long productId,@ModelAttribute("files") List<MultipartFile> files){
        try {
            Product existedProduct = productService.getProductById(productId);
            files = files==null?new ArrayList<MultipartFile>() : files;
            if(files.size()> ProductImage.MAXIMUM_IMAGES_PER_PRODUCT){
                return ResponseEntity.badRequest().body(localizationUtil.getLocalizedMessage(MessageKeys.UPLOAD_IMAGES_MAX_5));
            }
            List<ProductImage> productImages = new ArrayList<>();
            for (MultipartFile file : files){
                if(file.getSize() == 0){
                    continue;
                }
                if(file.getSize() >10 *1024 * 1024){
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(localizationUtil.getLocalizedMessage(MessageKeys.UPLOAD_IMAGES_FILE_LARGE));
                }
                String contentType = file.getContentType();
                if(contentType == null || contentType.startsWith("image/")){
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(localizationUtil.getLocalizedMessage(MessageKeys.UPLOAD_IMAGES_FILE_MUST_BE_IMAGE));
                }
                String filename = storeFile(file);
                ProductImage productImage = productService.createProductImage(existedProduct.getId(),
                        ProductImageDTO.builder()
                                .imageUrl(filename)
                                .build());
                productImages.add(productImage);
            }
            return ResponseEntity.ok().body(productImages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/images/{imageName}")
    public ResponseEntity<?> viewImage(@PathVariable String imageName){
        try {
            java.nio.file.Path imagePath = Paths.get("uploads/"+imageName);
            UrlResource resource = new UrlResource(imagePath.toUri());
            if(resource.exists()){
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(resource);
            }else {
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(new UrlResource(Paths.get("uploads/notfound.jfif").toUri()));
//                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    private String storeFile(MultipartFile file) throws IOException {
        if (isImageFile(file) || file.getOriginalFilename() == null) {
            throw new IOException("Invalid image format");
        }
        String fileName = Objects.requireNonNull(file.getOriginalFilename());
        // thêm uuid vào trước tên file để đảm bảo file là duy nhất không bị trùng
        String uniqueFilename = UUID.randomUUID().toString() + "_" + fileName;
        // Đường dẫn đến thư mục mà bạn muốn lưu file
        java.nio.file.Path upload =  Paths.get("uploads");
        // Kiểm tra và tạo thư mục nếu nó không tồn tại
        if(!Files.exists( upload)){
            Files.createDirectories(upload);
        }
        // Lấy đường dẫn đến file
        java.nio.file.Path destination = Paths.get(upload.toString(),uniqueFilename);
        //Sao chép file vào thư mục đích
        Files.copy(file.getInputStream(),destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }
    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null) {
            return false;
        }
        return contentType.startsWith("image/");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable long id,@Valid @RequestBody ProductDTO productDTO,BindingResult result){
        try {
            if(result.hasErrors()){
                List<String> errorMessage = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }
            Product product = productService.updateProduct(id,productDTO);
            return ResponseEntity.ok(product);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteCategory(@RequestParam long id){
        try {
            productService.deleteProduct(id);
            return  ResponseEntity.ok("delete product success with by id = " + id);
        }catch (Exception e){

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/productId/{id}")
    public ResponseEntity<?> getProductById(@PathVariable long id){
        try {
            Product product = productService.getProductById(id);
            return  ResponseEntity.ok(ProductResponse.convertResponse(product));
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/categoryId/{id}")
    public ResponseEntity<?> getProductByCategoryId(@PathVariable long id){
        try {
            List<ProductResponse> products = productService.findByCategory(id);
            return  ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
   // @PostMapping("/generateFakeProducts")
    private ResponseEntity<String> generateFakeProducts(){
        Faker faker = new Faker();

        for (int i=0;i<100;i++){
            String productName = faker.commerce().productName();
            if(productService.existsByName(productName)){
                continue;
            }
            ProductDTO productDTO = ProductDTO.builder()
                    .name(productName)
                    .price((float)faker.number().numberBetween(10,100000000))
                    .description(faker.lorem().sentence())
                    .categoryId((long)faker.number().numberBetween(1,4))
                    .thumbnail("")
                    .build();
            try {
                productService.createProduct(productDTO);
            }catch (Exception e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.ok("Fake date Products successful");
    }
    @GetMapping("/{name}")
    public ResponseEntity<?> findByName(@RequestParam(value = "name",defaultValue = "duy") String name){
        List<ProductResponse> productResponseList = productService.findByName(name);
        return ResponseEntity.ok(productResponseList);
    }
}
