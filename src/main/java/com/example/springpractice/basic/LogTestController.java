package com.example.springpractice.basic;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// @Controller 는 반환값이 String 이면 view 이름으로 인식

// @RestController
// 반환값을 http 메세지 body에 데이터를 그대로 넣어 버림
// REST API 만들 때 핵심적인 컨트롤러
@RestController
@Slf4j // 아래의 로거 객체를 대신 만들어줌
public class LogTestController {


    //private final Logger log = LoggerFactory.getLogger(getClass()); // LogTestController.class 로도 가능

    @GetMapping("/log-test")
    public String logTest(){
        String name = "Spring";

        // 로그를 찍을 때 레벨을 정할 수 있음 (설정에서 노출될 로그레벨 설정 가능 )
        // trace > debug > info > warn > error
        log.trace("trace name = {}", name); // 개발 local pc
        log.debug("debug name = {}", name); // 개발 서버
        log.info("info name = {}", name); // 운영 서버
        log.warn("warn name = {}", name);
        log.error("error name = {}", name); // 에러 로그가 남을 시 주로 알림 or 별도로 파일에 남기는 등으로 사용


        return "OK";
    }
}
