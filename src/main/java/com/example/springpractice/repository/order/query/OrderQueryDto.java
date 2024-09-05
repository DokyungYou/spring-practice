package com.example.springpractice.repository.order.query;

import com.example.springpractice.domain.Address;
import com.example.springpractice.domain.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@EqualsAndHashCode(of = "orderId") // 컨트롤러에서 groupingBy 할 때의 기준
@AllArgsConstructor
public class OrderQueryDto {

    private Long orderId;
    private String name;
    private LocalDateTime orderedAt;
    private OrderStatus orderStatus;
    private Address address;
    private List<OrderItemQueryDto> orderItems;

    
    // 컬렉션 제외
    public OrderQueryDto(Long orderId, String name, LocalDateTime orderedAt, OrderStatus orderStatus, Address address) {
        this.orderId = orderId;
        this.name = name;
        this.orderedAt = orderedAt;
        this.orderStatus = orderStatus;
        this.address = address;
    }
}
