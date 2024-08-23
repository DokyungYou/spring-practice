package com.example.springpractice.repository.v2;

import com.example.springpractice.domain.Item;
import com.example.springpractice.repository.ItemSearchCondition;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.example.springpractice.domain.QItem.item;

@Repository
public class ItemQueryRepositoryV2 {

    private final JPAQueryFactory jpaQueryFactory;

    public ItemQueryRepositoryV2(EntityManager manager){
        jpaQueryFactory = new JPAQueryFactory(manager);
    }

    public List<Item> findAll(ItemSearchCondition condition){
        String itemName = condition.getItemName();
        Integer maxPrice = condition.getMaxPrice();

        return jpaQueryFactory.select(item)
                .from(item)
                .where(likeItemName(itemName), maxPrice(maxPrice))
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

}
