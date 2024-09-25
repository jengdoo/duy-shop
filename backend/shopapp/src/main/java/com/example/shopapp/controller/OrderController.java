package com.example.shopapp.controller;
import com.example.shopapp.Model.Order;
import com.example.shopapp.components.LocalizationUtil;
import com.example.shopapp.dto.OrderDTO;
import com.example.shopapp.dto.PaymentDTO;
import com.example.shopapp.response.OrderListResponse;
import com.example.shopapp.response.OrderPaymentResponse;
import com.example.shopapp.response.OrderResponse;
import com.example.shopapp.response.OrderResponseNew;
import com.example.shopapp.service.OrderService;
import com.example.shopapp.service.PaymentService;
import com.example.shopapp.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    private final OrderService orderService;
    private final LocalizationUtil localizationUtil;
    private final PaymentService paymentService;
    @PostMapping("")
    public ResponseEntity<?> createdOrder(@Valid @RequestBody OrderDTO orderDTO, BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> messageError = result.getFieldErrors().stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(messageError);
            }

            // Tạo đơn hàng
            OrderResponse orderResponse = orderService.createOrder(orderDTO);
            if ("cod".equals(orderDTO.getPaymentMethod())) {
                // Nếu phương thức thanh toán là COD, không cần tạo payment
                return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
            } else if ("vnpay".equals(orderDTO.getPaymentMethod())) {
                // Nếu là VNPay, tạo payment
               if(orderResponse.getId()!=null||orderResponse.getId()==orderDTO.getId()){
                   PaymentDTO paymentDTO = paymentService.createPayment(orderResponse);
                   // Chỉ trả về URL thanh toán nếu tạo payment thành công
                   return ResponseEntity.status(HttpStatus.CREATED)
                           .body(new OrderPaymentResponse(orderResponse, paymentDTO.getPaymentUrl()));
               }
               else {
                   return ResponseEntity.badRequest().body("Invalid payment method");
               }
            } else {
                return ResponseEntity.badRequest().body("Invalid payment method");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/user/{user_id}")
    public ResponseEntity<?> getOrdersUserId(@Valid @PathVariable("user_id") Long userId){
        try {
            List<OrderResponse> orderResponseList = orderService.getAllOrdersByUserId(userId);
            return ResponseEntity.ok(orderResponseList);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(@Valid @PathVariable("id") Long id){
        try {
            OrderResponse orderResponse = orderService.getOrder(id);
            return ResponseEntity.ok(orderResponse);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage()); }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrders(@Valid @PathVariable Long id,@Valid @ModelAttribute OrderDTO orderDTO){
       try {
           OrderResponse orderResponse = orderService.updateOrder(id,orderDTO);
           return ResponseEntity.ok(orderResponse);
       }catch (Exception e){
           return ResponseEntity.badRequest().body(e.getMessage());
       }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrders(@Valid @PathVariable Long id){
        orderService.deleteOrder(id);
        return ResponseEntity.ok(localizationUtil.getLocalizedMessage(MessageKeys.DELETE_ORDER_SUCCESSFULLY));
    }
    @GetMapping("/get-orders-by-keyword")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<OrderListResponse> getOrdersByKeyword(
            @RequestParam(defaultValue = "",required = false) String keyword,
            @RequestParam(defaultValue = "0",required = false) int page,
            @RequestParam(defaultValue = "10",required = false) int limit){
        Pageable pageable = PageRequest.of(page,limit, Sort.by("id").ascending());
        Page<OrderResponseNew> orders = orderService.getOrdersByKeyword(keyword,pageable);
        List<OrderResponseNew> orderResponses = orders.getContent();
        int totalPage = orders.getTotalPages();
        int pageCurrent = orders.getNumber();
        int pageSizeCurrent = orders.getSize();
        return ResponseEntity.ok(OrderListResponse.builder()
                .orderResponseList(orderResponses)
                .page(pageCurrent)
                .pageSize(pageSizeCurrent)
                .totalPage(totalPage)
                .build());
    }
    @PutMapping("status/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long id, @RequestBody String status) throws Exception {
        OrderResponse orderResponse = orderService.updateStatus(id, status);
        return ResponseEntity.ok(orderResponse);
    }
}
