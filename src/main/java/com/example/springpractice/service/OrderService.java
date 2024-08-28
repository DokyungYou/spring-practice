package com.example.springpractice.service;

import com.example.springpractice.OrderSearch;
import com.example.springpractice.domain.Delivery;
import com.example.springpractice.domain.Member;
import com.example.springpractice.domain.Order;
import com.example.springpractice.domain.OrderItem;
import com.example.springpractice.domain.enums.DeliveryStatus;
import com.example.springpractice.domain.item.Item;
import com.example.springpractice.repository.ItemRepository;
import com.example.springpractice.repository.MemberRepository;
import com.example.springpractice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     *     CascadeType.ALL
     *     Order를 persist -> 해당 변수 안에 있는 엔티티들도 강제 persist
     *
     *     @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
     *     private List<OrderItem> orderItems = new ArrayList<>();
     *
     *     @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
     *     @JoinColumn(name = "delivery_id")
     *     private Delivery delivery;
     *
     *     OrderItem, Delivery 는 Order에서만 참조하며, persist 해야하는 라이프사이클이 동일하기때문에 적용한 것
     *     OrderItem, Delivery가  Order 외의 곳에서도 참조하는 상황이라면 해당 속성을 적용하면 안됨
     *     이때는 별도의 레파지토리를 통해 persist 해줘야함
     *     
     *     구조가 머리에 잘 안들어오고 애매하다싶으면 우선 쓰지말고, 나중에 리팩토링 때 적용하는 걸 추천
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count){ // 예제 단순화를 위해 상품을 하나만 넘기게끔 함
        
        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);
        
        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        //delivery.setStatus(DeliveryStatus.READY);
        
        // 주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장 (CascadeType.ALL 를 지정해줘서 Order만 저장해도, OrderItem, Delivery도 같이 저장)
        orderRepository.save(order);
        return order.getId();
    }

    /**
     * 주문 취소
     */
    @Transactional
    public void cancelOrder(Long orderId){
        // 주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        
        // 주문 취소
        order.cancel();
    }


    //검색
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllByString(orderSearch);
    }

}
