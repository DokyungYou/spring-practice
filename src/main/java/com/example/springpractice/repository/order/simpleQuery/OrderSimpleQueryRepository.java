package com.example.springpractice.repository.order.simpleQuery;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class OrderSimpleQueryRepository {

    private final EntityManager entityManager;


    /**
     * dto로 바로 받는 상황
     * 쿼리 1번 호출
     *
     * 페치 조인과의 차이점:
     * select 절에서 원하는 데이터만 선택해서 조회
     * (성능최적화부분에서는 OrderRepository 의 findAllWithMemberDelivery() 보다 조금 더 좋을 수 있으나 재사용성 X)
     *
     * dto로 가져왔기때문에 데이터 변경이 불가
     * API 스펙에 맞춘 코드가 레파지토리에 들어가는 단점 (레파지토리는 가급적 순수한 엔티티를 조회하는 데에 쓰여야한다.)
     */
    public List<OrderSimpleQueryDto> findOrdersDtos(){

        //JPA는 기본적으로 Entity 나 Object만 반환할 수 있다. new Operation을 꼭 사용해야한다.
        return entityManager.createQuery(
                "select " +
                        " new com.example.springpractice.repository.order.simpleQuery" +
                        ".OrderSimpleQueryDto(o.id, m.name, o.orderDateTime, o.status, d.address)" + // IDE에서 Cannot resolve 가 뜨지만 정상작동
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d", OrderSimpleQueryDto.class
        ).getResultList();
    }
}
