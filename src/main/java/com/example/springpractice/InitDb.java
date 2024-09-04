package com.example.springpractice;

import com.example.springpractice.domain.*;
import com.example.springpractice.domain.enums.DeliveryStatus;
import com.example.springpractice.domain.item.Book;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * userA
 *  - JPA1 BOOK
 *  - JPA2 BOOK
 * userB
 *  - SPRING1 BOOK
 *  - SPRING2 BOOK
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class InitDb {

    private final InitService initService;

    /**
     * 스프링 라이프 사이클 문제로 해당 메서드에 직접 @Transactional 적용이 잘 안되기 떄문에 별도의 빈으로 등록해서 세팅
     */
    @PostConstruct
    public void init(){
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @RequiredArgsConstructor
    @Transactional
    static class InitService {

        private final EntityManager entityManager;

        public void dbInit1(){

            Member member = createMember("회원A", "서울", "1", "0000");
            entityManager.persist(member);

            Book book1 = new Book();
            book1.setName("JPA1");
            book1.setPrice(25_000);
            book1.setStockQuantity(100);
            entityManager.persist(book1);

            Book book2 = new Book();
            book2.setName("JPA1");
            book2.setPrice(30_000);
            book2.setStockQuantity(100);
            entityManager.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, book1.getPrice(), 2);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, book2.getPrice(), 3);

            //TODO: orderItem, delivery 는 따로 persist 를 안해줬는데, 쿼리가 나가는 이유 복습 & 정리
            Delivery delivery = new Delivery();
            delivery.setAddress(new Address( "서울", "1", "0000"));
            delivery.setStatus(DeliveryStatus.READY);

            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            entityManager.persist(order);
        }

        public void dbInit2(){

            Member member = createMember("회원A", "서울", "1", "0000");
            entityManager.persist(member);

            Book book1 = new Book();
            book1.setName("SPRING1");
            book1.setPrice(40_000);
            book1.setStockQuantity(100);
            entityManager.persist(book1);

            Book book2 = new Book();
            book2.setName("SPRING2");
            book2.setPrice(35_000);
            book2.setStockQuantity(100);
            entityManager.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, book1.getPrice(), 2);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, book2.getPrice(), 3);

            //TODO: orderItem, delivery 는 따로 persist 를 안해줬는데, 쿼리가 나가는 이유 복습 & 정리
            Delivery delivery = new Delivery();
            delivery.setAddress(new Address( "강원도", "1", "0000"));
            delivery.setStatus(DeliveryStatus.READY);

            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            entityManager.persist(order);
        }

        private static Member createMember(String name, String city, String street, String zipcode) {
            Member member = new Member();
            member.setName(name);
            member.setAddress(new Address(city, street, zipcode));
            return member;
        }
    }
}
