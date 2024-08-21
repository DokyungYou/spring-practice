package com.example.springpractice;

import com.example.springpractice.config.MemoryConfig;
import com.example.springpractice.repository.ItemRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@Import(MemoryConfig.class)
@SpringBootApplication(scanBasePackages = "com.example.springpractice.web") // 해당 경로와 하위경로를 스캔
public class SpringPracticeApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringPracticeApplication.class, args);
    }

    @Bean
    @Profile("local") // 프로퍼티 설정이 local 일 때만 Bean으로 등록가능
    public TestDataInit testDataInit(ItemRepository itemRepository) {
        return new TestDataInit(itemRepository);
    }
}
