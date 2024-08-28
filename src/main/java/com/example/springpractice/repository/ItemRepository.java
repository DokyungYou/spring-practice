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

    /**
     * merge 는 쓰지 않는다고 생각하는게 좋다.
     */
    public void save(Item item){
        if(item.getId() == null){ // 새로 생성한 객체
            entityManager.persist(item);
        } else{
            // merge의 파라미터로 넣은 item은 영속성 컨텍스트로 변화 X
            // merge로 반환된 item 은 영속성 컨텍스트에서 관리되는 객체
            // 저장 후에 뭔가 더 사용할 일이 있다면 반환된 item을 써야한다.
            Item merge = entityManager.merge(item); // 서로 다르다
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
