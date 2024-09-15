package com.example.shopapp.service.implement;

import com.example.shopapp.Model.*;
import com.example.shopapp.dto.CartItemDTO;
import com.example.shopapp.dto.OrderDTO;
import com.example.shopapp.dto.OrderDetailDTO;
import com.example.shopapp.exception.DataNotFoundException;
import com.example.shopapp.repositories.OrderDetailRepo;
import com.example.shopapp.repositories.OrderRepo;
import com.example.shopapp.repositories.ProductRepo;
import com.example.shopapp.repositories.UserRepo;
import com.example.shopapp.response.OrderResponse;
import com.example.shopapp.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepo orderRepo;
    private final OrderDetailRepo orderDetailRepo;
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;
    private final ProductRepo productRepo;
    @Override
    public OrderResponse createOrder(OrderDTO orderDTO) throws Exception {
        // Tìm người dùng
        User user = userRepo.findById(orderDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("Cannot find user with id: " + orderDTO.getUserId()));

        // Tạo một đối tượng Order từ OrderDTO
        Order order = new Order();
        modelMapper.typeMap(OrderDTO.class, Order.class).addMappings(mapper -> mapper.skip(Order::setId));
        modelMapper.map(orderDTO, order);
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.PENDING);

        // Kiểm tra ngày giao hàng
        LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now() : orderDTO.getShippingDate();
        if (shippingDate.isBefore(LocalDate.now())) {
            throw new DataNotFoundException("Date must be at least today!");
        }
        order.setShippingDate(shippingDate);
        order.setActive(true);
        order.setTotalMoney(orderDTO.getTotalMoney());

        // Lưu đơn hàng
        orderRepo.save(order);

        // Tạo danh sách OrderDetail
        List<OrderDetail> orderDetails = new ArrayList<>();
        List<Product> productsToUpdate = new ArrayList<>();

        for (CartItemDTO cartItemDTO : orderDTO.getCartItems()) {
            Long productId = cartItemDTO.getProductId();
            int quantity = cartItemDTO.getQuantity();

            // Tìm sản phẩm
            Product product = productRepo.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

            // Kiểm tra số lượng tồn kho
            if (product.getQuantity() <= 0 || product.getQuantity() - quantity < 0) {
                throw new RuntimeException("Số lượng sản phẩm tồn kho không đủ hoặc sản phẩm hết hàng.");
            }

            // Cập nhật số lượng tồn kho
            product.setQuantity(product.getQuantity() - quantity);
            productsToUpdate.add(product);

            // Thiết lập thông tin cho OrderDetail
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setNumberOfProducts(quantity);
            orderDetail.setProduct(product);
            orderDetail.setPrice(product.getPrice());
            orderDetail.setTotalMoney(product.getPrice() * quantity);

            // Thêm OrderDetail vào danh sách
            orderDetails.add(orderDetail);
        }

        // Lưu tất cả OrderDetail
        orderDetailRepo.saveAll(orderDetails);

        // Cập nhật tất cả sản phẩm
        productRepo.saveAll(productsToUpdate);

        // Trả về kết quả
        return OrderResponse.convertRespo(order);
    }

    @Override
    public OrderResponse getOrder(Long id) {
        Order order = orderRepo.findById(id).orElseThrow(()->new RuntimeException("Not found order with id:"+id));
        return OrderResponse.convertRespo(order);
    }

    @Override
    public OrderResponse updateOrder(Long id, OrderDTO orderDTO) {
        Order existedOrder = orderRepo.findById(id).orElseThrow(()->new RuntimeException("Not found order with id:"+id));
        User existedUser = userRepo.findById(orderDTO.getUserId()).orElseThrow(()->new RuntimeException("Not found order with id:"+orderDTO.getUserId()));
        modelMapper.typeMap(OrderDTO.class,Order.class).addMappings(mapper ->mapper.skip(Order::setId));
        modelMapper.map(orderDTO,existedOrder);
        existedOrder.setUser(existedUser);
        return OrderResponse.convertRespo(orderRepo.save(existedOrder));
    }

    @Override
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
