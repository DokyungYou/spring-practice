package com.example.springpractice.controller;

import com.example.springpractice.type.IpPort;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HelloController {

    @GetMapping("/hello-v1")
    public String helloV1(HttpServletRequest request){
        // Http 요청 파라미터는 모두 문자로 처리되기때문에 변환과정이 필요
        String data = request.getParameter("data"); // 문자 타입 조회
        Integer intValue = Integer.valueOf(data); // 숫자 타입으로 변경
        log.info("intValue ={}", intValue);
        return "OK";
    }


    /**
     *  스프링이 문자타입의 숫자를 Integer 타입으로 변환해줌 (타입 컨버터 사용)
     *  타입에 맞지 않는 값으로 요청하면 DefaultHandlerExceptionResolver 가 예외처리
     */
    @GetMapping("/hello-v2")
    public String helloV2(@RequestParam Integer data){
        log.info("data ={}", data);
        return "OK";
    }


    @GetMapping("/ip-port")
    public String ipPort(@RequestParam IpPort ipPort){
        log.info("ipPort ={}", ipPort);
        return "OK";
    }


}
