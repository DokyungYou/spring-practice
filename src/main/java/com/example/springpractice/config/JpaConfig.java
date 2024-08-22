package com.example.springpractice.config;

import com.example.springpractice.repository.ItemRepository;
import com.example.springpractice.repository.jpa.JpaItemRepository;
import com.example.springpractice.service.ItemService;
import com.example.springpractice.service.ItemServiceV1;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class JpaConfig {

    private final EntityManager entityManager;


    @Bean
    public ItemRepository itemRepository(){
        return new JpaItemRepository(entityManager);
    }

    @Bean
    public ItemService itemService(){
        return new ItemServiceV1(itemRepository());
    }

}
