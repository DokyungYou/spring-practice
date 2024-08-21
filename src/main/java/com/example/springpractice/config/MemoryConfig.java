package com.example.springpractice.config;

import com.example.springpractice.repository.ItemRepository;
import com.example.springpractice.repository.memory.MemoryItemRepository;
import com.example.springpractice.service.ItemService;
import com.example.springpractice.service.ItemServiceV1;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // 컴포넌트 스캔 대상으로 안 잡고, SpringBootApplication 에서 Import 로 설정하였음
public class MemoryConfig {

    /**
     * 해당 실습에서는 service 와 repository 의 구현체를 편리하게 변경하기 위해 수동으로 빈을 등록하였음 
     */

    @Bean
    public ItemService itemService() {
        return new ItemServiceV1(itemRepository());
    }

    @Bean
    public ItemRepository itemRepository() {
        return new MemoryItemRepository();
    }

}
