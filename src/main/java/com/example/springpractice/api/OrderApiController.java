package com.example.springpractice.api;

import com.example.springpractice.OrderSearch;
import com.example.springpractice.domain.Address;
import com.example.springpractice.domain.Order;
import com.example.springpractice.domain.OrderItem;
import com.example.springpractice.domain.enums.OrderStatus;
import com.example.springpractice.repository.order.OrderRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class OrderApiController {

    private final OrderRepository orderRepository;

    /** 엔티티 직접 노출
     * - 양방향 관계 문제 발생 (json 무한루프 )-> @JsonIgnore
     * - jackson과 프록시 객체간의 문제 발생 -> Hibernate5Module 모듈 등록, LAZY=null 처리
     *
     * InitDb 로 조회될 ORDER은 2개인 상황 ( 2개가 서로 다른 구매자(Member) )
     *
     *  (1)
     *  from orders o1_0 join member m1_0 on m1_0.member_id=o1_0.member_id
     *
     *  (N 루프)
     *  from member m1_0 where m1_0.member_id=?
     *  from delivery d1_0 where d1_0.delivery_id=?
     *  from orders o1_0 where o1_0.delivery_id=?
     *  from order_item oi1_0 where oi1_0.order_id=?
     *  
     *  orderItems 루프 (현재 실습 초기화 데이터가 Order 하나당 OrderItem 2개 들어있는 상황)
     *  from item i1_0 where i1_0.item_id=?
     *  from item i1_0 where i1_0.item_id=? 
     */
    @GetMapping("/v1/orders")
    public List<Order> ordersV1(){
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());

        // lazy 강제 초기화
        for (Order order : orders) {
            order.getMember().getName();
            order.getDelivery().getStatus();

            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream()
                    .forEach(orderItem -> orderItem.getItem().getName());
        }

        return orders;
    }

    /**
     *SQL 실행 수 (V1과 같음)
     * order  1번
     * member , address  N번 (order 조회 수 만큼)
     * orderItem  N번  (order 조회 수 만큼)
     * item  N번 (orderItem 조회 수 만큼)
     */
    @GetMapping("/v2/orders")
    public List<OrderDto> ordersV2(){
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());

        List<OrderDto> collect = orders.stream()
                //.map(order -> new OrderDto(order))
                .map(OrderDto::new)
                .collect(Collectors.toList());
        return collect;
    }

    @Getter
    static class OrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderedAt;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;


        // DTO 세팅하면서 Lazy 초기화됨
        public OrderDto(Order order){
            this.orderId = order.getId();
            this.name = order.getMember().getName();
            this.orderedAt = order.getOrderDateTime();
            this.orderStatus = order.getStatus();
            this.address = order.getDelivery().getAddress();

            this.orderItems = order.getOrderItems()
                    .stream()
                    .map(orderItem -> new OrderItemDto(orderItem))
                    .collect(Collectors.toList());
        }
    }

    @Getter
    static class OrderItemDto {

        private String itemName;
        private int orderPrice;
        private int count;

        // DTO 세팅하면서 Lazy 초기화됨
        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }
}
