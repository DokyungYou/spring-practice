package com.example.springpractice.singleton;

import com.example.springpractice.AppConfig;
import com.example.springpractice.member.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SingletonTest {

    @Test
    @DisplayName("순수한 DI컨테이너 (스프링 X)")
    void pureContainer(){
        AppConfig appConfig = new AppConfig();

        // 1. 조회: 호출할 때마다 객체 생성
        MemberService memberService1 = appConfig.memberService();
        // 2. 조회: 호출할 때마다 객체 생성
        MemberService memberService2 = appConfig.memberService();

        Assertions.assertThat(memberService1).isNotSameAs(memberService2);
    }

    @Test
    @DisplayName("싱글톤 패턴을 적용한 객체 (스프링 X)")
    void singletonServiceTest(){
        SingletonService instance1 = SingletonService.getInstance();
        SingletonService instance2 = SingletonService.getInstance();

        // isSameAs ->  ==
        // isEqualTo -> .equals()
        Assertions.assertThat(instance1).isSameAs(instance2);
    }
}
