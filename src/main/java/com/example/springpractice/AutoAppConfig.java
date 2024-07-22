package com.example.springpractice;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan( // @Component 가 붙은 클래스를 찾아서 자동으로 bean 등록
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class) // @Configuration 이 붙은 놈을 제외 (수동으로 bean 등록했던 거 제외하기위함)
)
public class AutoAppConfig {
}
