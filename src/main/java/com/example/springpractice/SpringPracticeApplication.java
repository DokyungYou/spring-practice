package com.example.springpractice;

import com.example.springpractice.config.JdbcTemplateV1Config;
import com.example.springpractice.config.JdbcTemplateV2Config;
import com.example.springpractice.config.JdbcTemplateV3Config;
import com.example.springpractice.config.MemoryConfig;
import com.example.springpractice.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

//@Import(MemoryConfig.class)
//@Import(JdbcTemplateV1Config.class)
//@Import(JdbcTemplateV2Config.class)
@Import(JdbcTemplateV3Config.class)
@Slf4j
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

    @Bean
    @Profile("test") // 프로퍼티 설정이 test 일 때만 Bean으로 등록 (이 때는 해당 데이터소스가 기본으로 사용됨)
    public DataSource dataSource(){
        
        log.info("메모리 테이터베이스 초기화");
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");

        // mem:db -> jvm 내에 데이터베이스를 만듬 (임베디드 모드(메모리 모드)로 동작하는 h2 db 사용가능)
        // DB_CLOSE_DELAY=-1 -> 임베디드 모드에서의 db 커넥션 연결이 모두 끊어지면 db도 종료되는 문제를 방지
        dataSource.setUrl("jdbc:h2:mem:db;DB_CLOSE_DELAY=-1");
        
        dataSource.setUsername("sa");
        dataSource.setPassword("0829");
        return dataSource;
    }
}
