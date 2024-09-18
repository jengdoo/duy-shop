package com.example.shopapp.response;

import lombok.*;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderListResponse {
    private List<OrderResponseNew> orderResponseList;
    private int page;
    private int pageSize;
    private int totalPage;
}
