package com.example.shopapp.Model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_detailes")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    @ManyToOne
    @JoinColumn(name = "product_id",nullable = false)
    private Product product;
    @Column(name = "price",nullable = false)
    private Float price;
    @Column(name = "number_of_products",nullable = false)
    private int numberOfProducts;
    @Column(name = "total_money",nullable = false)
    private Float totalMoney;
    @Column(name = "color")
    private String color;
}
