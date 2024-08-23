package com.example.springpractice.service;

import com.example.springpractice.domain.Item;
import com.example.springpractice.repository.ItemSearchCondition;
import com.example.springpractice.repository.ItemUpdateDto;
import com.example.springpractice.repository.v2.ItemQueryRepositoryV2;
import com.example.springpractice.repository.v2.ItemRepositoryV2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class ItemServiceV2 implements ItemService {

    private final ItemRepositoryV2 itemRepositoryV2;
    private final ItemQueryRepositoryV2 itemQueryRepositoryV2;

    @Override
    public Item save(Item item) {
        return itemRepositoryV2.save(item);
    }


    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        Item item = itemRepositoryV2.findById(itemId).orElseThrow();
        item.setItemName(updateParam.getItemName());
        item.setPrice(updateParam.getPrice());
        item.setQuantity(updateParam.getQuantity());
    }

    @Override
    public Optional<Item> findById(Long id) {
        return itemRepositoryV2.findById(id);
    }

    @Override
    public List<Item> findItems(ItemSearchCondition cond) {
        return itemQueryRepositoryV2.findAll(cond);
    }
}
