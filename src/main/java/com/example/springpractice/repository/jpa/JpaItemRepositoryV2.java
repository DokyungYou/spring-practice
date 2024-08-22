package com.example.springpractice.repository.jpa;

import com.example.springpractice.domain.Item;
import com.example.springpractice.repository.ItemRepository;
import com.example.springpractice.repository.ItemSearchCondition;
import com.example.springpractice.repository.ItemUpdateDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository // 예외 변환 AOP 적용대상이 된다.
@Transactional // jpa에서 데이터를 변경 시엔 항상 트랜잭션이 있어야한다 (일반적으로는 서비스 계층에 걸어주는 것임)
public class JpaItemRepositoryV2 implements ItemRepository {

    private final SpringDataJpaItemRepository itemRepository;

    @Override
    public Item save(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        Item item = itemRepository.findById(itemId).orElseThrow();
        item.setItemName(updateParam.getItemName());
        item.setPrice(updateParam.getPrice());
        item.setQuantity(updateParam.getQuantity());

        // itemRepository.save(item); // 수정 후 저장을 안해줘도 실제로 db 업데이트 쿼리가 나간다
    }

    @Override
    public Optional<Item> findById(Long id) {
        return itemRepository.findById(id);
    }

    @Override
    public List<Item> findAll(ItemSearchCondition condition) { // jpql

        String itemName = condition.getItemName();
        Integer maxPrice = condition.getMaxPrice();

        // 실제로 이런식으로 하면안되고 동적쿼리를 만들어야함
        if(StringUtils.hasText(itemName) && maxPrice != null){
            //return itemRepository.findByItemNameLikeAndPriceLessThanEqual(itemName, maxPrice);
            return itemRepository.findItems("%" + itemName +"%", maxPrice);
        }else if(StringUtils.hasText(itemName)){
            return itemRepository.findByItemNameLike("%"+ itemName +"%");
        }else if (maxPrice != null){
            return itemRepository.findByPriceLessThanEqual(maxPrice);
        }
        return itemRepository.findAll();
    }
}
