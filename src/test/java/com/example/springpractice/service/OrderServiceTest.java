package com.example.springpractice.service;

import com.example.springpractice.domain.Address;
import com.example.springpractice.domain.Member;
import com.example.springpractice.domain.Order;
import com.example.springpractice.domain.enums.OrderStatus;
import com.example.springpractice.domain.item.Book;
import com.example.springpractice.domain.item.Item;
import com.example.springpractice.exception.NotEnoughStockException;
import com.example.springpractice.repository.ItemRepository;
import com.example.springpractice.repository.order.OrderRepository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@Transactional
@SpringBootTest
class OrderServiceTest {

    @Autowired EntityManager entityManager;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;
    @Autowired ItemRepository itemRepository;

    @DisplayName("상품주문")
    @Test
    void order_success(){
        //given
        Member member = createMember("회원1", new Address("서울", "길가", "000-000"));
        Item book = createBook("이펙티브 자바", 36_000, 10);
        int orderCount = 10;

        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order findOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.ORDER, findOrder.getStatus());
        assertEquals(1, findOrder.getOrderItems().size());
        assertEquals(book.getPrice() * orderCount, findOrder.getTotalPrice());
    }

    @DisplayName("상품주문_재고수량초과")
    @Test
    void order_fail(){
        //given
        Member member = createMember("회원1", new Address("서울", "길가", "000-000"));
        Item book = createBook("이펙티브 자바", 36_000, 10);
        int orderCount = 11;

        //when & then
        assertThatThrownBy(() -> orderService.order(member.getId(), book.getId(), orderCount))
                .isInstanceOf(NotEnoughStockException.class);

    }

    @DisplayName("주문취소")
    @Test
    void cancelOrder_success(){
        //given
        Member member = createMember("회원1", new Address("서울", "길가", "000-000"));
        Item book = createBook("이펙티브 자바", 36_000, 10);
        int orderCount = 3;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
        log.info("after order stockQuantity={}", book.getStockQuantity()); // 7

        //when
        orderService.cancelOrder(orderId);
        log.info("after cancel stockQuantity={}", book.getStockQuantity()); // 10

        //then
        Order findOrder = orderRepository.findOne(orderId);
        assertEquals(OrderStatus.CANCEL, findOrder.getStatus());
        assertEquals(10, book.getStockQuantity());
    }

    private Member createMember(String memberName, Address address) {
        Member member = new Member();
        member.setName(memberName);
        member.setAddress(address);
        entityManager.persist(member);
        return member;
    }

    private Item createBook(String itemName, int price, int stockQuantity) {
        Item book = new Book();
        book.setName(itemName);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        entityManager.persist(book);
        return book;
    }
}