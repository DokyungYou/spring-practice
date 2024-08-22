package com.example.springpractice.repository.mybatis;

import com.example.springpractice.domain.Item;
import com.example.springpractice.repository.ItemRepository;
import com.example.springpractice.repository.ItemSearchCondition;
import com.example.springpractice.repository.ItemUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MyBatisRepository implements ItemRepository {

    private final ItemMapper itemMapper;

    @Override
    public Item save(Item item) {

        // ItemMapper 는 인터페이스인데 어떻게 작동을 하는가? -> 동적 프록시 객체를 빈으로 등록한다.
        log.info("itemMapper class={}", itemMapper.getClass()); // itemMapper class=class jdk.proxy4.$Proxy71

        itemMapper.save(item);
        return item;
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        itemMapper.update(itemId, updateParam);
    }

    @Override
    public Optional<Item> findById(Long id) {
        return itemMapper.findById(id);
    }

    @Override
    public List<Item> findAll(ItemSearchCondition condition) {
       return itemMapper.findAll(condition);
    }
}
