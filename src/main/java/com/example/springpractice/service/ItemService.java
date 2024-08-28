package com.example.springpractice.service;

import com.example.springpractice.controller.BookForm;
import com.example.springpractice.domain.item.Book;
import com.example.springpractice.domain.item.Item;
import com.example.springpractice.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item){
        itemRepository.save(item);
    }


    /**
     * merge 시의 동작방식와 동일
     *
     * 주의: 변경 감지 기능을 사용하면 원하는 속성만 선택해서 변경할 수 있으나
     * merge 을 사용하면 모든 속성이 변경된
     * merge 시 값이 없으면 null 로 업데이트 할 위험 (merge 모든 필드를 교체)
     */
    @Transactional
    public Item updateItem(Long itemId, BookForm form){

        // 1. id 값을 기반으로 실제 db에 있는 영속상태의 엔티티를 찾아옴
        Item findItem = itemRepository.findOne(itemId);
        
        // 2. 영속 엔티티의 값을 준영속 엔티티의 값으로 모두 교체 (병합) (form -> findItem)
        findItem.setPrice(form.getPrice());
        findItem.setStockQuantity(form.getStockQuantity());
        findItem.setName(form.getName());

        // 3. 트랜잭션 커밋 시점에 변경 감지 기능 동작, 업데이트 쿼리 자동 실행

        return findItem;
    }


    public List<Item> findItems(){
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId){
        return itemRepository.findOne(itemId);
    }
}
