package com.example.springpractice.domain.item;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRepository {

    // 사실 싱글톤 빈이라 필드에 static final 안써줘도 됨
    private static final Map<Long, Item> store = new HashMap<>(); // 실제는 동시성 문제로 ConcurrentHashMap 써야한다
    private static long sequence = 0L; // 실제는 동시성 문제로 AtomicLong 같은 걸 써야함

    public Item save(Item item){
        item.setId(++sequence);
        store.put(item.getId(), item);
        return item;
    }

    public Item findById(Long id){
        return store.get(id);
    }

    public List<Item> findAll(){
        return new ArrayList<>(store.values());
    }

    public void updateItem(Long itemId, Item updateParam){ // 원래는 용도에 맞는 dto를 만들어서 사용해야함
        Item item = findById(itemId);
        item.setName(updateParam.getName());
        item.setPrice(updateParam.getPrice());
        item.setQuantity(updateParam.getQuantity());
    }

    // 테스트용
    public void clearStore(){
        store.clear();
    }

}
