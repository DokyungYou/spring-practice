package com.example.springpractice.repository.order.query;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    /**
     * 최적화
     * Query: 루트 1번, 컬렉션 1번
     * 데이터를 한꺼번에 처리할 때 많이 사용하는 방식
     *
     */
    public List<OrderQueryDto> findAllByDto_optimization() {

        //루트 조회(toOne 코드를 모두 한번에 조회)
        List<OrderQueryDto> orders = findOrders();

        // orderId 추출
        List<Long> orderIds = orders.stream()
                .map(order -> order.getOrderId())
                .collect(Collectors.toList());

        // 추출한 orderId에 맞는 orderItems를 조회 (where in 으로 한번에 조회)
        List<OrderItemQueryDto> orderItems = entityManager.createQuery(
                        "select new com.example.springpractice.repository.order.query" +
                                ".OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                                " from OrderItem oi" +
                                " join oi.item i" +
                                " where oi.order.id in :orderIds", OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();

        // orderItem 컬렉션을 orderId 별로 분리
        Map<Long, List<OrderItemQueryDto>> orderItemMap =
                orderItems.stream()
                        .collect(Collectors.groupingBy(OrderItemQueryDto::getOrderId));

        // 루프를 돌면서 컬렉션 추가(추가 쿼리 실행X)
        // 해당 orderId 에 맞는 orderItems를 세팅
        orders.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));

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
