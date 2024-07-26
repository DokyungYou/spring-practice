package com.example.springpractice.web;

import com.example.springpractice.common.MyLogger;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class LogDemoController {

    private final LogDemoService logDemoService;

    // 웹 스코프를 가지는 bean ( 프록시설정이 아닌채로 쓰면 컨테이너 생성 후 싱글톤 빈의 의존관계 주입 시에 문제가 생김)
    // 프록시 가짜 객체로 의존관계 주입 (실제 객체의 조회를 지연처리)
    private final MyLogger myLogger;
    
    // 의존관계 주입을 MyLogger 날것이 아닌 ObjectProvider로 넣어줌
    // request scope bean 의 생성 지연
    //private final ObjectProvider<MyLogger> myLoggerProvider;



    @RequestMapping("log-demo") // 해당 url로 요청이 오면 아래의 메서드가 호출되고, myLogger 에 url 세팅
    @ResponseBody
    public String logDemo(HttpServletRequest request){ // 자바에서 제공하는 표준 서블릿 규약이 있는데 그것에 의한 http - request 정보를 받을 수 있음
        String requestUrl = request.getRequestURL().toString();// 클라이언트가 어떤 url로 요청했는지 알 수 있음

        //MyLogger myLogger = myLoggerProvider.getObject(); // 이 시점에 bean 이 만들어짐


        // 프록시 모드 설정시 가짜 클래스를 넣은 것을 볼 수 있음
        // myLogger.getClass(): class com.example.springpractice.common.MyLogger$$SpringCGLIB$$0
        System.out.println("myLogger.getClass(): "+ myLogger.getClass());

        // 사실 이런 로직은 공통처리가 가능한 인터셉터나 서블릿 필터 같은 곳을 활용하는 것이 좋음
        myLogger.setRequestUrl(requestUrl); // 프록시 객체의 메서드 호출 (프록시는 진짜 객체의 메서드를 호출)
        myLogger.log("컨트롤러 테스트");

        logDemoService.logic("testId");
        return "OK";
    }}
