package com.example.springpractice.repository;

import com.example.springpractice.OrderSearch;
import com.example.springpractice.domain.Member;
import com.example.springpractice.domain.Order;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class OrderRepository {

    private final EntityManager entityManager;

    public void save(Order order){
        entityManager.persist(order);
    }

    public Order findOne(Long id){
       return entityManager.find(Order.class, id);
    }

    // jpql은 동적쿼리에 취약하다 (dsl를 쓰도록하자)
    public List<Order> findAllByString(OrderSearch orderSearch) {
        //language=JPAQL
        String jpql = "select o From Order o join o.member m";
        boolean isFirstCondition = true;
        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }
        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }
        TypedQuery<Order> query = entityManager.createQuery(jpql, Order.class)
                .setMaxResults(100); //최대 100건
        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }
        return query.getResultList();
    }


    // JPA Criteria는 JPA 표준 스펙이지만 사람이 쓰라고 만든게 아닌 듯 하다 (dsl을 쓰자)
    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Order, Member> m = o.join("member", JoinType.INNER); //회원과 조인
        List<Predicate> criteria = new ArrayList<>();
        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"),
                    orderSearch.getOrderStatus());
            criteria.add(status);
        }
        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name =
                    cb.like(m.<String>get("name"), "%" + orderSearch.getMemberName()
                            + "%");
            criteria.add(name);
        }
        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = entityManager.createQuery(cq).setMaxResults(100); //최대 100건
        return query.getResultList();
    }

    /**
     * 성능 문제의 90%는 N + 1문제
     *
     * 기본적으로 Lazy로 깔고 필요한 것만 패치조인으로
     * 객체 그래프를 묶어서 db에서 한방에 가져오면 대부분의 성능 문제가 해결된다.
     */
    public List<Order> findAllWithMemberDelivery(OrderSearch orderSearch) {
        // order를 가져올 때 객체그래프로 한 번에 가져오고 싶은 상황
        // lazy를 무시하고 프록시가 아닌 실제 값을 채워서 가져옴
       return entityManager.createQuery(
                "select o from Order o" +
                            " join fetch o.member" +
                            " join fetch o.delivery", Order.class
               ).getResultList();
    }
}
