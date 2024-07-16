package com.example.springpractice;

import com.example.springpractice.repository.JdbcTemplateMemberRepository;
import com.example.springpractice.repository.MemberRepository;
import com.example.springpractice.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class SpringConfig {

    private DataSource dataSource;

    @Autowired
    public SpringConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public MemberService memberService(){
        return new MemberService(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository(){
        //return new MemoryMemberRepository();
        return new JdbcTemplateMemberRepository(dataSource); // 구현체만 바꿔끼면 된다. (서비스 로직은 변경 x, OCP원칙)
    }
}
