package com.example.springpractice.config;

import com.example.springpractice.repository.ItemRepository;
import com.example.springpractice.repository.jpa.JpaItemRepositoryV2;
import com.example.springpractice.repository.jpa.SpringDataJpaItemRepository;
import com.example.springpractice.service.ItemService;
import com.example.springpractice.service.ItemServiceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SpringDataJpaConfig {

    private final SpringDataJpaItemRepository springDataJpaItemRepository;

    @Bean
    public ItemRepository itemRepository(){
        return new JpaItemRepositoryV2(springDataJpaItemRepository);
    }

    @Bean
    public ItemService itemService(){
        return new ItemServiceV1(itemRepository());
    }

}
