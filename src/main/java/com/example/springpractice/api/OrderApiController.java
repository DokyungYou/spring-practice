package com.example.springpractice.api;

import com.example.springpractice.OrderSearch;
import com.example.springpractice.domain.Address;
import com.example.springpractice.domain.Order;
import com.example.springpractice.domain.OrderItem;
import com.example.springpractice.domain.enums.OrderStatus;
import com.example.springpractice.repository.order.OrderRepository;
import com.example.springpractice.repository.order.query.OrderQueryDto;
import com.example.springpractice.repository.order.query.OrderQueryRepository;
import com.example.springpractice.repository.order.simpleQuery.OrderSimpleQueryDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

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
     * item  M번 (orderItem 조회 수 만큼)
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

    /** 컬렉션 페치 조인 적용 버전
     *
     * InitDb 세팅한 ORDER은 2개인 상황 ( 2개가 서로 다른 구매자(Member), ORDER 하나당 서로다른 item 2개씩 세팅 )
     *
     * from orders o1_0
     *  join member m1_0 on m1_0.member_id=o1_0.member_id
     *  join delivery d1_0 on d1_0.delivery_id=o1_0.delivery_id
     *
     * orderItem개수만큼 order가 중복된 상태로 조회 (조회 row 4개 -> json도 2개의 order가 아닌 4개가 나오게 됨)
     * 참고: 스프링부트3(하이버네이트 6버전)일 경우 distinct 자동적용돼서 2개로 조회)
     *
     *  join order_item oi1_0 on o1_0.order_id=oi1_0.order_id
     *  join item i1_0 on i1_0.item_id=oi1_0.item_id
     */
    @GetMapping("/v3/orders")
    public List<OrderDto> ordersV3(){
        List<Order> orders = orderRepository.findAllWithItem();

        for (Order order : orders) {
            log.info("order ref ={}, order id ={}", order, order.getId());
        }

        List<OrderDto> collect = orders.stream()
                //.map(order -> new OrderDto(order))
                .map(OrderDto::new)
                .collect(Collectors.toList());


        return collect;
    }

    /**
     * jpa.hibernate.hibernate.default_batch_fetch_size 설정
     *
     *
     *  from orders o1_0
     *  join member m1_0 on m1_0.member_id=o1_0.member_id
     *  join delivery d1_0 on d1_0.delivery_id=o1_0.delivery_id
     *  offset limit
     *
     *  -lazy 초기화 (배치)
     *  from order_item oi1_0  where oi1_0.order_id in (? 배치 사이즈)
     *
     *  -lazy 초기화 (배치)
     *  from item i1_0 where i1_0.item_id in (? 배치 사이즈)
     */
    @GetMapping("/v3.1/orders")
    public List<OrderDto> ordersV3_page(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "10") int limit
            ){

        // ToOne 관계인 애들은 페치조인으로 한방에 가져온다.
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);

        List<OrderDto> collect = orders.stream()
                //.map(order -> new OrderDto(order))
                .map(OrderDto::new)
                .collect(Collectors.toList());


        return collect;
    }

    /** Order이 2개인 상태
     *
     * (1)
     * from orders o1_0
     * join member m1_0 on m1_0.member_id=o1_0.member_id
     * join delivery d1_0on d1_0.delivery_id=o1_0.delivery_id
     * 
     * (N)
     * from order_item oi1_0
     * join item i1_0 on i1_0.item_id=oi1_0.item_id where oi1_0.order_id=?
     * 
     * ToOne 관계는 먼저 조회 후 ToMany 관계는 각각 별도처리
     */
    @GetMapping("/v4/orders")
    public List<OrderQueryDto> ordersV4(){
        return orderQueryRepository.findOrderQueryDtos();
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
