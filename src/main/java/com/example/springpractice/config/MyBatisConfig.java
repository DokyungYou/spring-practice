package com.example.springpractice.config;

import com.example.springpractice.repository.ItemRepository;
import com.example.springpractice.repository.jdbctemplate.JdbcTemplateItemRepositoryV3;
import com.example.springpractice.repository.mybatis.ItemMapper;
import com.example.springpractice.repository.mybatis.MyBatisRepository;
import com.example.springpractice.service.ItemService;
import com.example.springpractice.service.ItemServiceV1;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
//@MapperScan(basePackages= {"com.example.springpractice.repository.mybatis"})
public class MyBatisConfig {

    // 마이바티스 모듈이 데이터소스나 트랜잭션 매니저 같은 걸 다 읽어서 맵퍼랑 다 연결시켜줌
   // private final DataSource dataSource;

    /*
    * 일단 인텔리제이화면에서  ItemMapper 를 빈으로 인식을 못하고있다고 떠서,
    * 버전도 낮춰보고, @MapperScan도 적용하는 등의 방법을 취해봤으나 여전히 IDE에서 인식을 못함
    * 근데 문제없이 정상실행 되고있다.
    * 로그도 찍어봤는데 잘 나온다.  itemMapper class=class jdk.proxy4.$Proxy71
    *
    * 버전도 원복하고, @MapperScan 도 없앴는데, 정상실행되는 것을 확인했다.
    * */

    private final ItemMapper itemMapper;


    @Bean
    public ItemRepository itemRepository(){
        return new MyBatisRepository(itemMapper);
    }

    @Bean
    public ItemService itemService(){
        return new ItemServiceV1(itemRepository());
    }

}
