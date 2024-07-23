package com.example.springpractice.autowired;

import com.example.springpractice.member.service.MemberService;
import jakarta.annotation.Nullable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Optional;

public class AutowiredTest {

    @Test
    void autowiredOption(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TestBean.class); // TestBean 클래스를 Bean으로 등록 뒤 얘를 기준으로 컴포넌트 스캔


        /* 결과
        setNoBean1: 실행자체가 안됨
        setNoBean2: null
        setNoBean3: Optional.empty
        */
    }

    static class TestBean{ // 해당 컨테이너 내에서 Bean이 아닌 요소를 넣어봤음

        @Autowired(required = false) // 의존관계가 없으면 메서드 호출 자체가 안됨
        public void setNoBean1(MemberService noBean){
            System.out.println("setNoBean1: " + noBean);
        }

        @Autowired
        public void setNoBean2(@Nullable MemberService noBean){
            System.out.println("setNoBean2: " + noBean);
        }

        @Autowired
        public void setNoBean3(Optional<MemberService> noBean){
            System.out.println("setNoBean3: " + noBean);
        }
    }
}
