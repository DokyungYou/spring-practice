package com.example.springpractice.singleton;

import com.example.springpractice.AppConfig;
import com.example.springpractice.member.repository.MemberRepository;
import com.example.springpractice.member.service.MemberServiceImpl;
import com.example.springpractice.order.OrderServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.*;

public class ConfigurationSingletonTest {

    @Test
    void test(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

        MemberServiceImpl memberService = ac.getBean("memberService", MemberServiceImpl.class);
        OrderServiceImpl orderService = ac.getBean("orderService", OrderServiceImpl.class);

        MemberRepository memberRepository = ac.getBean("memberRepository", MemberRepository.class);
        MemberRepository memberRepository1 = memberService.getMemberRepository();
        MemberRepository memberRepository2 = orderService.getMemberRepository();

        assertThat(memberRepository).isEqualTo(memberRepository1);
        assertThat(memberRepository).isEqualTo(memberRepository2);
        assertThat(memberRepository1).isEqualTo(memberRepository2);
    }
}
