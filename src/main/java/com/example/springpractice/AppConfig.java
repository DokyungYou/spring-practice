package com.example.springpractice;


import com.example.springpractice.discount.DiscountPolicy;
import com.example.springpractice.discount.FixedDiscountPolicy;
import com.example.springpractice.member.repository.MemberRepository;
import com.example.springpractice.member.repository.MemoryMemberRepository;
import com.example.springpractice.member.service.MemberService;
import com.example.springpractice.member.service.MemberServiceImpl;
import com.example.springpractice.order.OrderService;
import com.example.springpractice.order.OrderServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 애플리케이션의 전체 동작 방식을 구성
// 구현객체 생성, 연결하는 책임을 가지는 별도의 설정 클래스
@Configuration
public class AppConfig {

    // @Configuration 이 있어야 싱글톤 보장
    // @Configuration 없이 @Bean 사용이 가능하나, 싱글톤 보장 x
    // 아래 코드에서 memberRepository() 가 여러번 호출돼버릴 것임

    @Bean
    public MemberService memberService(){
        System.out.println("AppConfig.memberService 호출");
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public OrderService orderService(){
        System.out.println("AppConfig.orderService 호출");
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    @Bean
    public MemberRepository memberRepository(){
        System.out.println("AppConfig.memberRepository 호출");
        return new MemoryMemberRepository();
    }
    @Bean
    public DiscountPolicy discountPolicy(){
        return new FixedDiscountPolicy();
    }

}
