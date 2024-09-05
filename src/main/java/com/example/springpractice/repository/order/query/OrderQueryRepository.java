package com.example.springpractice.repository.order.query;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class OrderQueryRepository {

    private final EntityManager entityManager;

    /**
     *  ToOne 관계는 먼저 조회 후 ToMany 관계는 각각 별도처리
     */
    public List<OrderQueryDto> findOrderQueryDtos() {
        List<OrderQueryDto> orders = findOrders(); // 쿼리 1번
        orders.forEach(o -> {
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId()); // N번
            o.setOrderItems(orderItems);
        });

        return orders;
    }

    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return entityManager.createQuery(
                "select new com.example.springpractice.repository.order.query" +
                        ".OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                        " from OrderItem oi" +
                        " join oi.item i" +
                        " where oi.order.id = :orderId", OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    private List<OrderQueryDto> findOrders() {
        // 컬렉션은 패스
        return entityManager.createQuery(
                        "select new com.example.springpractice.repository.order.query.OrderQueryDto(" +
                                "o.id, m.name, o.orderDateTime, o.status, m.address)" +
                                " from Order o " +
                                " join o.member m " +
                                " join o.delivery d", OrderQueryDto.class)
                .getResultList();
    }
}
