package com.example.springpractice.config;

import com.example.springpractice.repository.ItemRepository;
import com.example.springpractice.repository.jdbctemplate.JdbcTemplateItemRepositoryV1;
import com.example.springpractice.repository.jdbctemplate.JdbcTemplateItemRepositoryV2;
import com.example.springpractice.service.ItemService;
import com.example.springpractice.service.ItemServiceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class JdbcTemplateV2Config {

    /**
     * 스프링부트는 DataSource 와 트랜잭션매니저 를 자동으로 등록해줌
     * (대신 프로퍼티에 spring.datasource 설정을 해줘야함)
     */
    private final DataSource dataSource;

    @Bean
    public ItemRepository itemRepository(){
        return new JdbcTemplateItemRepositoryV2(dataSource);
    }

    @Bean
    public ItemService itemService(){
        return new ItemServiceV1(itemRepository());
    }

}
