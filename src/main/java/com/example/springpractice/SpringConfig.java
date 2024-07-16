package com.example.springpractice;

import com.example.springpractice.aop.TimeTraceAop;
import com.example.springpractice.repository.JdbcTemplateMemberRepository;
import com.example.springpractice.repository.JpaMemberRepository;
import com.example.springpractice.repository.MemberRepository;
import com.example.springpractice.service.MemberService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class SpringConfig {

    /*private DataSource dataSource;

    @PersistenceContext // Entity Manager 를 주입받기 위해 사용되는 어노테이션
    private EntityManager entityManager;

    @Autowired
    public SpringConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }
*/

    private final MemberRepository memberRepository;

    @Autowired // 사실 생성자가 하나면 생략이 가능하지만 공부중이라 넣어줬다.
    public SpringConfig(MemberRepository memberRepository) {
        // public interface SpringDataJpaMemberRepository extends JpaRepository<Member, Long>, MemberRepository
        // 해당 인터페이스의 구현체가 자동으로 만들어지고 이미 빈에 등록돼있는 상태이기때문에 주입이 가능하다.

        this.memberRepository = memberRepository;
    }

    @Bean
    public MemberService memberService(){
        //return new MemberService(memberRepository());
        return new MemberService(memberRepository);
    }

/*    @Bean
    public MemberRepository memberRepository(){
        //return new MemoryMemberRepository();
        // return new JdbcTemplateMemberRepository(dataSource); // 구현체만 바꿔끼면 된다. (서비스 로직은 변경 x, OCP원칙)
        //return new JpaMemberRepository(entityManager);
    }*/


//    @Bean
//    public TimeTraceAop timeTraceAop(){
//        return new TimeTraceAop();
//    }
}
