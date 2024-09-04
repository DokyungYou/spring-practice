package com.example.springpractice.api;

import com.example.springpractice.OrderSearch;
import com.example.springpractice.domain.Order;
import com.example.springpractice.domain.OrderItem;
import com.example.springpractice.repository.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
     *  from item i1_0 where i1_0.item_id=?
     *  from item i1_0 where i1_0.item_id=? // 왜 한번 더 나오지
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
}
