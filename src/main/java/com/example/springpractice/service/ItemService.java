package com.example.springpractice.service;


import com.example.springpractice.domain.Item;
import com.example.springpractice.repository.ItemSearchCondition;
import com.example.springpractice.repository.ItemUpdateDto;

import java.util.List;
import java.util.Optional;

/**
 *  서비스는 구현체를 변경할 일이 많지는 않기 때문에 인터페이스를 도입하는 일은 드뭄
 *  해당 실습에서는 구현체를 변경할 예정이기때문에 인터페이스를 도입하였음
 */
public interface ItemService {

    Item save(Item item);

    void update(Long itemId, ItemUpdateDto updateParam);

    Optional<Item> findById(Long id);

    List<Item> findItems(ItemSearchCondition itemSearch);
}
