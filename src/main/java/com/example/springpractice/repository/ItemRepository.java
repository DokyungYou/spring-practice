package com.example.springpractice.repository;

import com.example.springpractice.domain.item.Item;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ItemRepository {

    private final EntityManager entityManager;

    public void save(Item item){
        if(item.getId() == null){ // 새로 생성한 객체
            entityManager.persist(item);
        } else{
            entityManager.merge(item);
        }
    }

    public Item findOne(Long id){
        return entityManager.find(Item.class, id);
    }

    public List<Item> findAll(){
        return entityManager.createQuery("select i from Item i", Item.class)
                .getResultList();
    }


}
