package com.example.springpractice.domain;

import com.example.springpractice.domain.item.Item;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter //Setter 지양
@Entity
public class OrderItem {

    @Id @GeneratedValue
    @OrderColumn(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; // 주문 당시의 가격

    private int count; // 주문 수량
}
