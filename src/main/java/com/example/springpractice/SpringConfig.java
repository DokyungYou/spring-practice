package com.example.springpractice;

import com.example.springpractice.repository.MemberRepository;
import com.example.springpractice.repository.MemoryMemberRepository;
import com.example.springpractice.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    @Bean
    public MemberService memberService(){
        return new MemberService(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository(){
        return new MemoryMemberRepository();
    }
}
