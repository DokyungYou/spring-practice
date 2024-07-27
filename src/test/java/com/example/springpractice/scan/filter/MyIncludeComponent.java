package com.example.springpractice.scan.filter;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target(ElementType.TYPE) // TYPE은 클래스 레벨
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyIncludeComponent { // @MyIncludeComponent 이 붙은 것은 컴포넌트 스캔에 추가하겠다.


}
