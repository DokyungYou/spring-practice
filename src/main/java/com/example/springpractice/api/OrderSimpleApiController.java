package com.example.springpractice.api;

import com.example.springpractice.OrderSearch;
import com.example.springpractice.domain.Order;
import com.example.springpractice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    @GetMapping("/v1/simple-order")
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
}
