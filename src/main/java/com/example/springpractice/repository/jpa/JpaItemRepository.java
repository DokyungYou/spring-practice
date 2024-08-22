package com.example.springpractice.repository.jpa;

import com.example.springpractice.domain.Item;
import com.example.springpractice.repository.ItemRepository;
import com.example.springpractice.repository.ItemSearchCondition;
import com.example.springpractice.repository.ItemUpdateDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository // 예외 변환 AOP 적용대상이 된다.
@Transactional // jpa에서 데이터를 변경 시엔 항상 트랜잭션이 있어야한다 (일반적으로는 서비스 계층에 걸어주는 것임)
public class JpaItemRepository implements ItemRepository {

    /**
     * EntityManager 가 있어야 JPA
     * JPA의 모든 동작은 EntityManager를 통해서 이루어진다.
     * EntityManager 내부에 데이터소스를 가지고 있고, db에 접근할 수 있음
     *
     * EntityManager를 만들 때, 데이터소스도 넣는 등의 과정을 EntityManagerFactory 라는 걸 가지고 세팅을 복잡하게 하는데,
     * (JPA 설정 시엔  EntityManagerFactory, JPA 트랜잭션 매니저, 데이터 소스 등 다양한 설정이 필요)
     * 스프링부트랑 통합했기 때문에 자동으로 만들어준다.
     * 이런 복잡한 과정을 자동으로 해주기때문에 그냥 쓰면 된다.
     */
    private final EntityManager entityManager;

    public JpaItemRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Item save(Item item) {
        entityManager.persist(item); // persist: 영구히 보존한다라는 느낌

        // JPA 가 쿼리문 실행 후에 생성 된 ID결과를 받아서 넣어준다.
        log.info("saved item ={}", item);
        return item;
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        Item item = entityManager.find(Item.class, itemId);
        item.setItemName(updateParam.getItemName());
        item.setPrice(updateParam.getPrice());
        item.setQuantity(updateParam.getQuantity());
        
        //entityManager.persist(item); // 수정 후 저장을 안해줘도 실제로 db 업데이트 쿼리가 나간다

        /**
         * 내부 조회 시점에 미리 스냅샷을 떠놓고 어떤 데이터가 바뀌었는지 JPA가 안다
         * 그래서 이것을 언제 db에 업테이트 쿼리를 날리는가?
         * 트랜잭션이 커밋되는 시점에 변경된 엔티티 객체가 있는 지 확인 후 특정 엔티티 객체가 변경된 경우 해당 데이터들에 대해 업데이트 쿼리 실행
         *
         * 그러면 JPA가 어떻게 변경 된 엔티티 객체를 찾는가? -> 영속성 컨텍스트라는 JPA 내부 원리 이해 필요
         */
    }

    @Override
    public Optional<Item> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Item.class, id));
    }


    /** JPQL (Java Persistence Query Language)
     * 하나를 조회할때는 식별자(pk)를 기반으로 조회하면 되는데,
     * 여러가지 조건으로 쿼리를 짜야하는 경우에는 JPQL (sql이 아닌데 sql과 95% 비슷)
     * 객체 쿼리 언어
     *
     * JPQL 문법은 sql과 거의 비슷한데 테이블을 대상으로 하는 것이 아닌, 엔티티를 대상으로 실행된다.
     * JPQL 실행 시 그 안에 포함 된 엔티티 객체의 매핑 정보를 활용하여 SQL를 만듬
     * 
     * 근데 얘도 동적쿼리에 약함
     */
    @Override
    public List<Item> findAll(ItemSearchCondition condition) { // jpql
        
        // 이 안의 Item은 테이블이 아닌 자바객체를 뜻함
        // Item 에 i라는 alias를 붙였고,
        // select i는 컬럼도 아니고 Item 객체(엔티티) 그 자체를 뜻함
        String jpql = "select i from Item i";
        
        // 동적쿼리 만들기 허허...
        String itemName = condition.getItemName();
        Integer maxPrice = condition.getMaxPrice();

        if (StringUtils.hasText(itemName) || maxPrice != null) {
            jpql += " where";
        }

        boolean andFlag = false;
        if(StringUtils.hasText(itemName)){
            jpql += " i.itemName like concat('%',:itemName,'%')";
            andFlag = true;
        }

        if (maxPrice != null) {
            if (andFlag) {
                jpql += " and";
            }
            jpql += " i.price <= :maxPrice";
        }
        log.info("jpql={}", jpql);
        TypedQuery<Item> query = entityManager.createQuery(jpql, Item.class);
        if (StringUtils.hasText(itemName)) {
            query.setParameter("itemName", itemName);
        }
        if (maxPrice != null) {
            query.setParameter("maxPrice", maxPrice);
        }
        return query.getResultList();

    }
}
