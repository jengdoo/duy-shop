package com.example.shopapp.controller;
import com.example.shopapp.Model.OrderDetail;
import com.example.shopapp.components.LocalizationUtil;
import com.example.shopapp.dto.OrderDTO;
import com.example.shopapp.dto.OrderDetailDTO;
import com.example.shopapp.response.OrderDetailResponse;
import com.example.shopapp.service.OrderDetailService;
import com.example.shopapp.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/order_details")
public class OrderDetailController {
    private final OrderDetailService orderDetailService;
    private final LocalizationUtil localizationUtil;
    @PostMapping("")
    public ResponseEntity<?> createdOrderDetail(@Valid @ModelAttribute OrderDetailDTO orderDetailDTO, BindingResult result){
        try {
            if(result.hasErrors()){
                List<String> messageError = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(messageError);
            }
            OrderDetailResponse orderDetailResponse= orderDetailService.createOrderDetail(orderDetailDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(orderDetailResponse);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(@Valid @PathVariable("id") Long id){
        try {
            OrderDetailResponse orderDetailResponse = orderDetailService.getOrderDetail(id);
            return ResponseEntity.ok(orderDetailResponse);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    // lấy ra danh sách orderdetail của 1 order bất kì
    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetailByOrderId(@Valid @PathVariable("orderId") Long orderId){
        try {
            List<OrderDetailResponse> orderDetailResponseList= orderDetailService.getOrderDetailByOrderId(orderId);
            return ResponseEntity.ok(orderDetailResponseList);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(@PathVariable Long id,@Valid @ModelAttribute OrderDetailDTO orderDetailDTO){
        try {
            OrderDetailResponse orderDetailResponse = orderDetailService.updateOrderDetail(id,orderDetailDTO);
            return ResponseEntity.ok(orderDetailResponse);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetail(@Valid @PathVariable Long id){
        try {
            orderDetailService.deleteOrderDetail(id);
            return ResponseEntity.ok(localizationUtil.getLocalizedMessage(MessageKeys.DELETE_ORDER_DETAIL_SUCCESSFULLY));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("not find order detail with id:"+id+" ,so not delete");
        }
    }
}
