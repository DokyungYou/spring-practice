package com.example.springpractice.basic.request;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Map;

@Controller
@Slf4j
public class RequestParamController {

    @RequestMapping("/request-param-v1")
    public void requestParamV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userName = request.getParameter("userName");
        int age = Integer.parseInt(request.getParameter("age"));
        log.info("userName ={}, age={}", userName, age);

        response.getWriter().write("OK");
    }

    @RequestMapping("/request-param-v2")
    @ResponseBody
    public String requestParamV2(@RequestParam("username") String userName,
                                 @RequestParam("age") int userAge){  // 변수명이 동일하면

        log.info("userName ={}, age={}", userName, userAge);
        return "OK"; // @Controller에서 @ResponseBody 를 적용하지 않을 땐 Ok 라는 view를 찾게된다.

    }

    /*
    아래 방식들은 이름을 생략했으나, 이름을 항상 적어주는 것을 권장
    */
    @RequestMapping("/request-param-v3")
    @ResponseBody
    public String requestParamV3(@RequestParam String username,
                                 @RequestParam int age){ // 이름을 생략한 대신 요청 파라미터 이름과 동일해야함

        log.info("userName ={}, age={}", username, age);
        return "OK";
    }


    /**
     * String, int, Integer 등의 단순 타입이면 @RequestParam 생략 가능
     * @RequestParam 을 생략한 대신 요청 파라미터 이름과 동일해야함
     */
    @RequestMapping("/request-param-v4")
    @ResponseBody
    public String requestParamV4(String username, int age){

        log.info("userName ={}, age={}", username, age);
        return "OK";
    }

    @RequestMapping("/request-param-required")
    @ResponseBody
    public String requestParamRequired(
                                 @RequestParam(required = true) String username, //  ?username= 이렇게 빈 문자열로 요청해도 에러발생 안하는 점 주의
                                 @RequestParam(required = false) Integer age){ // required = false로 해놨으나 int로 받게끔하면 에러발생 (기본타입에는 null이 못 들어감)

        log.info("userName ={}, age={}", username, age);
        return "OK";
    }


    /**
     * defaultValue를 설정할때는 required 설정이 필요 X (넘기건 안 넘기건 값이 넘어옴)
     */
    @RequestMapping("/request-param-default")
    @ResponseBody
    public String requestParamDefault(
                                 @RequestParam(defaultValue = "guest") String username, // 빈 문자열로 넘겨도 defaultValue 로 넘어온다
                                 @RequestParam(defaultValue = "-1") int age){ //  기본값을 설정해줬으니 null 값이 넘어오지 않기때문에 기본타입 ok

        log.info("userName ={}, age={}", username, age);
        return "OK";
    }




    // 보통 파라미터는 한 개를 쓰는 경우가 훨씬 많음
    /** 파라미터를 Map으로 조회하기 - requestParamMap
     * @RequestParam Map, MultiValueMap
     * Map(key=value)
     * MultiValueMap(key=[value1, value2, ...]) ex) (key=userIds, value=[id1, id2])
     *
     * 파라미터의 값이 1개가 확실하다면 Map 을 사용해도 되지만, 그렇지 않다면 MultiValueMap 을 사용
     */
    @RequestMapping("/request-param-map")
    @ResponseBody
    public String requestParamMap(@RequestParam Map<String, Object> paramMap){

        log.info("userName ={}, age={}", paramMap.get("username"), paramMap.get("age"));
        return "OK";
    }


}
