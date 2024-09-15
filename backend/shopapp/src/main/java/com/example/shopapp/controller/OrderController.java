package com.example.shopapp.controller;
import com.example.shopapp.Model.Order;
import com.example.shopapp.components.LocalizationUtil;
import com.example.shopapp.dto.OrderDTO;
import com.example.shopapp.response.OrderResponse;
import com.example.shopapp.service.OrderService;
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
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    private final OrderService orderService;
    private final LocalizationUtil localizationUtil;
    @PostMapping("")
    public ResponseEntity<?> createdOrder(@Valid @RequestBody OrderDTO orderDTO, BindingResult result){
        try {
            if(result.hasErrors()){
                List<String> messageError = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(messageError);
            }
            OrderResponse orderResponse = orderService.createOrder(orderDTO);
                return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
        }catch (Exception e){
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
}
