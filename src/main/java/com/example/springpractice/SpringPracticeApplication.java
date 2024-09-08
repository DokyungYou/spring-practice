package com.example.springpractice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;
import java.util.UUID;

@EnableJpaAuditing
@SpringBootApplication
//@EnableJpaRepositories(basePackages = "com.example.springpractice.repository") 원래는 이런식으로 세팅이 필요한데, 스프링부트는 X
public class SpringPracticeApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringPracticeApplication.class, args);
    }

    /**
     *  @CreatedBy, @LastModifiedBy
     *  등록 및 수정이 될 때마다
     *  해당 빈을 호출
     *
     * 해당 실습에서는 UUID로 사용했지만,
     * 실무에서는 세션 정보나, 스프링 시큐리티 로그인 정보에서 ID를 받음
     */
    @Bean
    public AuditorAware<String> auditorProvider() {
//        return new AuditorAware<String>() {
//            @Override
//            public Optional<String> getCurrentAuditor() {
//                return Optional.of(UUID.randomUUID().toString());
//            }
//        };
        return () -> Optional.of(UUID.randomUUID().toString());
    }
}
