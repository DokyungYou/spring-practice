package com.example.springpractice.web.login;

import com.example.springpractice.web.filter.LogFilter;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    // 이렇게 해놓으면 스프링부트가 WAS를 띄울 때 필터를 같이 넣어준다
    @Bean
    public FilterRegistrationBean logFilter(){
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogFilter());
        filterRegistrationBean.setOrder(1); // 필터체인 순서 지정
        filterRegistrationBean.addUrlPatterns("/*"); // 모든 url 요청에 적용

        return filterRegistrationBean;
    }
}
