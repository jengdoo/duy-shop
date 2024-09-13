package com.example.shopapp.service.implement;

import com.example.shopapp.Model.Order;
import com.example.shopapp.Model.OrderStatus;
import com.example.shopapp.Model.User;
import com.example.shopapp.dto.OrderDTO;
import com.example.shopapp.exception.DataNotFoundException;
import com.example.shopapp.repositories.OrderRepo;
import com.example.shopapp.repositories.UserRepo;
import com.example.shopapp.response.OrderResponse;
import com.example.shopapp.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepo orderRepo;
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;
    @Override
    @Transactional
    public OrderResponse createOrder(OrderDTO orderDTO) throws Exception {
        User user = userRepo.findById(orderDTO.getUserId()).orElseThrow(()->new RuntimeException("Cannot find user with id:"+orderDTO.getUserId()));
        // dùng thư viện model mapper
        // tạo một luồng ánh xạ riêng để kiểm soát việc ánh xạ
        modelMapper.typeMap(OrderDTO.class, Order.class).addMappings(mapper->mapper.skip(Order::setId));
        //cập nhật các trường của đơn hàng từ orderDTO
        Order order = new Order();
        modelMapper.map(orderDTO,order);
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.PENDING);
        LocalDate shippingDate = orderDTO.getShippingDate() == null? LocalDate.now():orderDTO.getShippingDate();
        if(shippingDate.isBefore(LocalDate.now())){
            throw new DataNotFoundException("Date must be at last today!");
        }
        order.setShippingDate(shippingDate);
        order.setActive(true);
        orderRepo.save(order);
        return OrderResponse.convertRespo(order);
    }

    @Override
    public OrderResponse getOrder(Long id) {
        Order order = orderRepo.findById(id).orElseThrow(()->new RuntimeException("Not found order with id:"+id));
        return OrderResponse.convertRespo(order);
    }

    @Override
    @Transactional
    public OrderResponse updateOrder(Long id, OrderDTO orderDTO) {
        Order existedOrder = orderRepo.findById(id).orElseThrow(()->new RuntimeException("Not found order with id:"+id));
        User existedUser = userRepo.findById(orderDTO.getUserId()).orElseThrow(()->new RuntimeException("Not found order with id:"+orderDTO.getUserId()));
        modelMapper.typeMap(OrderDTO.class,Order.class).addMappings(mapper ->mapper.skip(Order::setId));
        modelMapper.map(orderDTO,existedOrder);
        existedOrder.setUser(existedUser);
        return OrderResponse.convertRespo(orderRepo.save(existedOrder));
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        Optional<Order> orderOptional = orderRepo.findById(id);
        if(orderOptional.isPresent()){
           Order order = orderOptional.get();
           order.setActive(false);
           orderRepo.save(order);
        }
    }

    @Override
    public List<OrderResponse> getAllOrdersByUserId(Long userId) {
        List<Order> order = orderRepo.findByUserId(userId);
        return order.stream().map(OrderResponse::convertRespo).toList();
    }
}
