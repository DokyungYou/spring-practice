package com.example.springpractice;

import com.example.springpractice.member.repository.MemberRepository;
import com.example.springpractice.member.repository.MemoryMemberRepository;
import com.example.springpractice.member.service.MemberService;
import com.example.springpractice.member.service.MemberServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan( // @Component 가 붙은 클래스를 찾아서 자동으로 bean 등록
        //basePackages = "com.example.springpractice.member", // 해당 패키지를 시작으로 스캔하게 됨 (설정이 없으면 모든 자바 코드를 스캔(라이브러리 포함))
        //basePackageClasses = AutoAppConfig.class, //  해당 클래스의 위치에서 시작 (com.example.springpractice)
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class) // @Configuration 이 붙은 놈을 제외 (수동으로 bean 등록했던 거 제외하기위함)
)
public class AutoAppConfig {

    // 디폴트 설정은 @ComponentScan이 붙은 해당 클래스의 위치에서 시작 (보통 프로젝트의 시작 루트에 해당클래스를 둔다)

    // 스프링부트의 경우 @SpringBootApplication(스프링 부트의 대표 시작 정보) 를 프로젝트 시작 위치에 두는 것이 관례
    // @SpringBootApplication 안에 @ComponentScan 이 있기때문에 @ComponentScan를 따로 사용할 일이 없음

    
    // 중복등록 테스트용
/*    @Bean(name = "memoryMemberRepository")
    MemberRepository memberRepository(){
        return new MemoryMemberRepository();
    }*/
}
