package com.example.springpractice.config;

import com.example.springpractice.repository.ItemRepository;
import com.example.springpractice.repository.jpa.JpaItemRepositoryV3;
import com.example.springpractice.repository.v2.ItemQueryRepositoryV2;
import com.example.springpractice.repository.v2.ItemRepositoryV2;
import com.example.springpractice.service.ItemService;
import com.example.springpractice.service.ItemServiceV2;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class V2Config {

    private final ItemRepositoryV2 itemRepositoryV2;
    private final EntityManager entityManager;

    @Bean
    public ItemQueryRepositoryV2 itemQueryRepositoryV2(){
        return new ItemQueryRepositoryV2(entityManager);
    }

    @Bean
    public ItemService itemService(){
        return new ItemServiceV2(itemRepositoryV2,itemQueryRepositoryV2());
    }


    // TestDataInit 동작 땜에 넣음
    @Bean
    public ItemRepository itemRepository(){
        return new JpaItemRepositoryV3(entityManager);
    }

}
