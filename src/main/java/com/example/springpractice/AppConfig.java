package com.example.springpractice;


import com.example.springpractice.discount.DiscountPolicy;
import com.example.springpractice.discount.FixedDiscountPolicy;
import com.example.springpractice.member.repository.MemberRepository;
import com.example.springpractice.member.repository.MemoryMemberRepository;
import com.example.springpractice.member.service.MemberService;
import com.example.springpractice.member.service.MemberServiceImpl;
import com.example.springpractice.order.OrderService;
import com.example.springpractice.order.OrderServiceImpl;

// 애플리케이션의 전체 동작 방식을 구성
// 구현객체 생성, 연결하는 책임을 가지는 별도의 설정 클래스
public class AppConfig {

    public MemberService memberService(){
        return new MemberServiceImpl(memberRepository());
    }

    public OrderService orderService(){
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }


    public MemberRepository memberRepository(){
        return new MemoryMemberRepository();
    }
    public DiscountPolicy discountPolicy(){
        return new FixedDiscountPolicy();
    }

}
