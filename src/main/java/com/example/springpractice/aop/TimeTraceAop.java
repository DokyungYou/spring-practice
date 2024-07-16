package com.example.springpractice.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component  // Component를 안 붙이고 Config에서 직접 Bean으로 등록했더니 순환참조 문제 발생 (자기자신도 AOP 대상으로 지정했기때문)
public class TimeTraceAop {

    @Around("execution(* com.example.springpractice..*(..))") // 해당 패키지 하위에 모두 적용
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable{
        long startTime = System.currentTimeMillis();
        System.out.println("START: " + joinPoint.toString()); // 어떤 메서드인지 나온다.
        try{
            return joinPoint.proceed();// 다음 메서드로 진행이 된다.
        }finally {
            long endTime = System.currentTimeMillis();
            long timeMs = endTime - startTime;
            System.out.println("END: " + joinPoint.toString() + timeMs + "ms");
        }

    }
}
