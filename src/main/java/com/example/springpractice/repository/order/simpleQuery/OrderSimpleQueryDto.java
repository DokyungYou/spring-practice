package com.example.springpractice.repository.order.simpleQuery;

import com.example.springpractice.domain.Address;
import com.example.springpractice.domain.Order;
import com.example.springpractice.domain.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
public class OrderSimpleQueryDto {

    private Long orderId;
    private String name;
    private LocalDateTime orderedAt;
    private OrderStatus orderStatus;
    private Address address;

    // DTO는 엔티티를 참조해도 무관
    public OrderSimpleQueryDto(Order order){
        orderId = order.getId();
        name = order.getMember().getName(); // Lazy 초기화 (.getMember() 까지는 프록시)
        orderedAt = order.getOrderDateTime();
        orderStatus = order.getStatus();
        address = order.getDelivery().getAddress(); // Lazy 초기화 (.getDelivery() 까지는 프록시)
    }

}
