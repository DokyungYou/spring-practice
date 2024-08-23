package com.example.springpractice.repository.jpa;

import com.example.springpractice.domain.Item;
import com.example.springpractice.domain.QItem;
import com.example.springpractice.repository.ItemRepository;
import com.example.springpractice.repository.ItemSearchCondition;
import com.example.springpractice.repository.ItemUpdateDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static com.example.springpractice.domain.QItem.*;

@Slf4j
@Repository // 예외 변환 AOP 적용대상이 된다.
@Transactional // jpa에서 데이터를 변경 시엔 항상 트랜잭션이 있어야한다 (일반적으로는 서비스 계층에 걸어주는 것임)
public class JpaItemRepositoryV3 implements ItemRepository {

    private final EntityManager entityManager;
    private final JPAQueryFactory jpaQueryFactory;

    public JpaItemRepositoryV3(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Item save(Item item) {
        entityManager.persist(item);
        return item;
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        Item item = entityManager.find(Item.class, itemId);
        item.setItemName(updateParam.getItemName());
        item.setPrice(updateParam.getPrice());
        item.setQuantity(updateParam.getQuantity());

        //entityManager.persist(item); // 수정 후 저장을 안해줘도 실제로 db 업데이트 쿼리가 나간다

    }

    @Override
    public Optional<Item> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Item.class, id));
    }



    public List<Item> findAllOld(ItemSearchCondition condition) { // jpql

        return jpaQueryFactory.select(item)
                .from(item)
                .where(getBooleanBuilder(condition))
                .fetch();
    }


    @Override
    public List<Item> findAll(ItemSearchCondition condition) {

        String itemName = condition.getItemName();
        Integer maxPrice = condition.getMaxPrice();

        return jpaQueryFactory.select(item)
                .from(item)
                .where(likeItemName(itemName), maxPrice(maxPrice)) // null 이면 무시
                .fetch();
    }

    private BooleanExpression likeItemName(String itemName){
        if(StringUtils.hasText(itemName)){
            return item.itemName.like("%" + itemName + "%");
        }
        return null;
    }

    private BooleanExpression maxPrice(Integer maxPrice){
        if(maxPrice != null){
            return item.price.loe(maxPrice);
        }
        return null;
    }

    private static BooleanBuilder getBooleanBuilder(ItemSearchCondition condition) {
        String itemName = condition.getItemName();
        Integer maxPrice = condition.getMaxPrice();

        //QItem item = new QItem("item"); // alias 지정해서 사용할 수도 있음
        // QItem.item -> static import 한 상태

        BooleanBuilder builder = new BooleanBuilder();
        if(StringUtils.hasText(itemName)){
            builder.and(item.itemName.like("%" + itemName + "%"));
        }
        if(maxPrice != null){
            builder.and(item.price.loe(maxPrice));
        }
        return builder;
    }

}
