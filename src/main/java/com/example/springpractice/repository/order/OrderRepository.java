package com.example.springpractice.repository.order;

import com.example.springpractice.OrderSearch;
import com.example.springpractice.domain.Member;
import com.example.springpractice.domain.Order;
import com.example.springpractice.repository.order.simpleQuery.OrderSimpleQueryDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * N + 1 실습 시에 default_batch_fetch_size 설정 off
 */
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
     *
     * OrderSimpleQueryRepository 의 findOrdersDtos() 와 비교:
     * 기본적으로 조인하는 것까지는 성능이 같으나 select 절에서 데이터를 더 많이 가져옴 -> 그럼 findOrdersDtos()가 더 좋은가? 아니다. 트레이드오프
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


    /** 
     * 컬렉션 페치 조인
     * 
     * 이런 식으로 order 가 중복돼서 조회 (order에 속한 orderItem 개수만큼 중복)
     * order_id | item_id
     * 1            1
     * 1            2
     * 2            3
     * 2            4
     *
     *
     * db의 distinct는 모든 컬럼의 값이 완전히 동일한 경우에만 적용
     * JPA의 distinct는 SQL에 distinct를 추가하고, 더해서 같은 엔티티가 조회되면, 애플리케이션에서 중복을 걸러줌
     * (결국 중복된 데이터 모두를 애플리케이션으로 끌고오기때문에 데이터 전송용 자체가 많아짐)
     *
     *
     * - 참고: 컬렉션 둘 이상에 페치 조인 사용하면 안됨(데이터 부정합하게 조회될 가능성)
     * row가 많아지는 문제를 떠나서 어떤 기준으로 데이터를 끌고와야할지를 모르게 될 수 있음
     *
     */
    public List<Order> findAllWithItem() {

        // 한 order에 여러개의 orderItem 있는 상태에서 조인을 하면 orderItem개수만큼 order가 중복됨
        // -> 가져오는 데이터가 배가 되어 온다..

        // distinct를 넣어줌으로 중복데이터 제거 (스프링부트3(하이버네이트 6버전)일 경우 안 적어도 distinct 자동적용)
        return entityManager.createQuery(
                "select distinct o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d" +

                        " join fetch o.orderItems oi" +
                        " join fetch oi.item i", Order.class
        ).getResultList();

    }

    /**
     * 컬렉션 페치 조인 주의점 - db에서 페이징 불가
     * 해당 쿼리를 날리면 db에서는 distinct 적용이 안되기때문에 일단 데이터가 중복된 상태로 조회 ( order의 기준자체가 틀어져버린다 )
     * 
     * -> 예를들어 order를 기준으로 페이징해서 2개의 데이터를 가져오고싶다.
     *  order_id | item_id
     *  1            1
     *  1            2
     *  2            3
     *  2            4
     *
     * -> 우리가 원하는 결과는 order_id가 1인 데이터1개,  order_id가 2인 데이터1개
     * 그러나 order_id가 1인 데이터2개를 가져와버리게 되는 것임
     *
     * -> 컬렉션 페치 조인 시 order의 기준자체가 틀어져버려서 제대로 된 페이징이 불가
     * -> order를 기준으로 limit 하고 싶었으나 order_item을 기준으로 limit돼버림
     * 
     *
     * 그래서 결국
     * 하이버네이트는 경고 로그를 남기면서 모든 데이터를 DB에서 읽어오고 애플리케이션으로 다 가져와버린 후
     * 메모리에서 페이징(매우 위험 , 메모리 부족)
     *
     * 자세한 내용은 자바 ORM 표준 JPA 프로그래밍 의 페치 조인 부분을 참고
     */
    public List<Order> findAllWithItemPaging() {

        return entityManager.createQuery(
                "select distinct o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d" +

                        " join fetch o.orderItems oi" +
                        " join fetch oi.item i", Order.class)
                
                // 페이징
                .setFirstResult(1)
                .setMaxResults(100)
                .getResultList();

    }


    /**
     *
     */
    public List<Order> findAllWithMemberDelivery(int offset, int limit) {

        // order입장에서 ToOne 관계인 member, delivery 는 페치조인으로 가져온다.
        // ToMany 관계는 lazy
        return entityManager.createQuery(
                "select o from Order o" +
                        " join fetch o.member" +
                        " join fetch o.delivery", Order.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }
}
