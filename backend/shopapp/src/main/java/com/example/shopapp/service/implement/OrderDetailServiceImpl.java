package com.example.shopapp.service.implement;

import com.example.shopapp.Model.Order;
import com.example.shopapp.Model.OrderDetail;
import com.example.shopapp.Model.Product;
import com.example.shopapp.dto.OrderDetailDTO;
import com.example.shopapp.repositories.OrderDetailRepo;
import com.example.shopapp.repositories.OrderRepo;
import com.example.shopapp.repositories.ProductRepo;
import com.example.shopapp.response.OrderDetailResponse;
import com.example.shopapp.response.OrderResponse;
import com.example.shopapp.service.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {
    private final OrderDetailRepo orderDetailRepo;
    private final OrderRepo orderRepo;
    private final ProductRepo productRepo;
    @Override
    public OrderDetailResponse createOrderDetail(OrderDetailDTO orderDetailDTO) {
        Order order = orderRepo.findById(orderDetailDTO.getOrderId()).orElseThrow(()->new RuntimeException("Not found order id:"+orderDetailDTO.getOrderId()));
        Product product = productRepo.findById(orderDetailDTO.getProductId()).orElseThrow(()->new RuntimeException("Not found product id:"+orderDetailDTO.getProductId()));
        OrderDetail orderDetail = OrderDetail.builder()
                .order(order)
                .product(product)
                .price(orderDetailDTO.getPrice())
                .numberOfProducts(orderDetailDTO.getNumberOfProducts())
                .totalMoney(orderDetailDTO.getTotalMoney())
                .color(orderDetailDTO.getColor())
                .build();
        return OrderDetailResponse.convertOrderDetailsResponse(orderDetailRepo.save(orderDetail));
    }

    @Override
    public OrderDetailResponse getOrderDetail(Long id) {
        return OrderDetailResponse.convertOrderDetailsResponse(orderDetailRepo.findById(id).orElseThrow(()->new RuntimeException("Not found order detail with id:"+id)));
    }

    @Override
    public OrderDetailResponse updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) {
        OrderDetail orderDetail = orderDetailRepo.findById(id).orElseThrow(()->new RuntimeException("not found order detail with id:"+id));
        Order order = orderRepo.findById(orderDetailDTO.getOrderId()).orElseThrow(()->new RuntimeException("Not found order id:"+orderDetailDTO.getOrderId()));
        Product product = productRepo.findById(orderDetailDTO.getProductId()).orElseThrow(()->new RuntimeException("Not found product id:"+orderDetailDTO.getProductId()));
        orderDetail.setPrice(orderDetailDTO.getPrice());
        orderDetail.setNumberOfProducts(orderDetailDTO.getNumberOfProducts());
        orderDetail.setTotalMoney(orderDetailDTO.getTotalMoney());
        orderDetail.setColor(orderDetailDTO.getColor());
        orderDetail.setOrder(order);
        orderDetail.setProduct(product);
        return OrderDetailResponse.convertOrderDetailsResponse(orderDetailRepo.save(orderDetail));
    }

    @Override
    public void deleteOrderDetail(Long id) {
        OrderDetail orderDetail = orderDetailRepo.findById(id).orElseThrow(()->new RuntimeException("not find order detail with id:"+id));
        if(orderDetail!=null){
            orderDetailRepo.delete(orderDetail);
        }
    }

    @Override
    public List<OrderDetailResponse> getOrderDetailByOrderId(Long orderId) {
        List<OrderDetail> orderDetailList = orderDetailRepo.findByOrderId(orderId);
        return orderDetailList.stream().map(OrderDetailResponse::convertOrderDetailsResponse).toList();
    }
}
