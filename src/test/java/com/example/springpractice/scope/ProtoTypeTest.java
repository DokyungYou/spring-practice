package com.example.springpractice.scope;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import static org.assertj.core.api.Assertions.*;

public class ProtoTypeTest {


    @Test
    void findProtoTypeBean(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ProtoTypeBean.class);  // @Component 지정을 안해도 컨테이너 생성자에 직접 넣어줬기 때문에  컴포넌트 스캔의 대상이 됨

        // 프로토타입 bean은 스프링컨테이너에 의해 요청할 때마다 새로 생성
        ProtoTypeBean protoTypeBean1 = ac.getBean(ProtoTypeBean.class);
        ProtoTypeBean protoTypeBean2 = ac.getBean(ProtoTypeBean.class);

        assertThat(protoTypeBean1).isNotEqualTo(protoTypeBean2);
        assertThat(protoTypeBean1).isNotSameAs(protoTypeBean2);

        // 싱글톤타입 bean 과 다르게 직접 호출
        protoTypeBean1.destroy();
        protoTypeBean2.destroy();

        ac.close();
    }

    @Scope("prototype")
    static class ProtoTypeBean {

        ProtoTypeBean(){
            System.out.println("ProtoTypeBean 참조값: " + this);
        }

        @PostConstruct
        public void init(){
            System.out.println("ProtoTypeBean init() 호출");
        }

        // 프로토타입의 빈은 생성과 의존관계 주입, 초기화 까지만 관여
        // 따라서 해당 콜백메서드는 자동실행 X (필요하면 직접호출해야함)
        @PreDestroy
        public void destroy(){
            System.out.println("ProtoTypeBean destroy() 호출");
        }
    }
}
