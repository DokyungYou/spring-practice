package com.example.springpractice.domain;

import com.example.springpractice.domain.item.Item;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.weaver.ast.Or;

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


    // 생성 메서드
    public static OrderItem createOrderItem(Item item, int orderPrice, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        // 상품 재고 소모
        item.removeStock(count);

        return orderItem;
    }

    // 비즈니스 로직
    public void cancel(){

        // 상품 재고를 원복
        item.addStock(count);
    }

    /**
     * 주문상품 전체 가격 조회
     */
    public int getTotalPrice() {
        return  orderPrice * count;
    }
    
}
