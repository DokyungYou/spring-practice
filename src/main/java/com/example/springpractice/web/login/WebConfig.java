package com.example.springpractice.web.login;

import com.example.springpractice.web.filter.LogFilter;
import com.example.springpractice.web.filter.LoginCheckFilter;
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

    @Bean
    public FilterRegistrationBean loginCheckFilter(){
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LoginCheckFilter());
        filterRegistrationBean.setOrder(2); // 필터체인 순서 지정

        // 이미 필터에 url패턴 검사하는 로직을 넣어놨기때문에, 모든 url 요청 적용으로 설정
        // 새로운 기능을 만들어도 일일히 설정을 바꾸지 않게 하기위함
        // 성능저하가 있지않을까 하지만, 데이터베이스쿼리, 네트워크 등에서 성능을 깎아먹지, 이런부분은 영향이 미미하다고 함
        filterRegistrationBean.addUrlPatterns("/*"); 

        return filterRegistrationBean;
    }
}
