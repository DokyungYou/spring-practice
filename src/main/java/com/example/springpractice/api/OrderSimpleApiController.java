package com.example.springpractice.api;

import com.example.springpractice.OrderSearch;
import com.example.springpractice.domain.Address;
import com.example.springpractice.domain.Order;
import com.example.springpractice.domain.enums.OrderStatus;
import com.example.springpractice.repository.OrderRepository;
import lombok.*;
import org.aspectj.weaver.ast.Or;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * XToOne(ManyToOne, OneToOne)
 * Order
 * Order -> Member
 * Order -> Delivery
 */
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    /** 엔티티를 그대로 노출하는 잘못된 방식이기때문에 아래 내용은 이런게 있구나~ 정도로 넘어가자..
     *
     * Order 를 가져옴 -> Order안에 Member가 있음 -> Member안에 Order가 있다. -> -> .... 무한루프
     * 
     * 무한루프 방지를 위헤 Order를 참조하는 곳에 @JsonIgnore -> jackson 관련 에러 발생
     * jackson 라이브러리는 기본적으로 프록시 객체를 json으로 어떻게 생성해야 하는지 모름
     *
     * @JsonIgnore 적용 전에는 프록시관련 에러가 아닌 무한루프에 빠지고, 적용 시에는 에러가 뜨는 이유?
     * 해당 문제는 jsonignore와 지연로딩 관련 참조
     */
    @GetMapping("/v1/simple-orders")
    public List<Order> ordersV1(){
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());

        // Lazy 강제 초기화
        for (Order order : orders) {
            // order.getMember() 까지는 프록시 -> getName() 할 때 db에 쿼리 날라가고 초기화됨
            order.getMember().getName();
            order.getDelivery().getAddress();
        }
        return orders;
    }

    /** 
     * InitDb 로 조회될 ORDER은 2개인 상황 ( 2개가 서로 다른 구매자(Member) )
     *
     *
     * N + 1 문제 발생 (첫번째 쿼리의 결과 N번 만큼 쿼리가 추가 실행되는 문제)
     * 첫쿼리:
     * from ORDER join member
     * 
     * ORDER 개수만큼 루프 (돌면서 하나씩 가져와서 Lazy 초기화)
     * from member where member_id = ?
     * from delivery where delivery_id = ?
     * from order where delivery_id = ?  // TODO 이건 왜 나오는지?
     *
     */
    @GetMapping("/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2(){ // 반환값을 한번 더 클래스로 감싸는 것을 추천

        // N + 1 문제 발생 -> (1) + Member (N) + Delivery (N)
        // Order 2개 조회 (1)
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());

        // member (N) + Delivery (N)
        List<SimpleOrderDto> collect = orders.stream()
                // .map(order -> new SimpleOrderDto(order))
                .map(SimpleOrderDto::new)
                .collect(Collectors.toList());

        return collect;
    }


    @Getter // 게터가 없으면 직렬화문제 발생
    @Setter
    @NoArgsConstructor
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderedAt;
        private OrderStatus orderStatus;
        private Address address;

        // DTO가 엔티티를 파라미터로 받는 것은 크게 문제되지 않음
        public SimpleOrderDto(Order order){
            orderId = order.getId();
            name = order.getMember().getName(); // Lazy 초기화 (.getMember() 까지는 프록시)
            orderedAt = order.getOrderDateTime();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); // Lazy 초기화 (.getDelivery() 까지는 프록시)
        }
    }

}
