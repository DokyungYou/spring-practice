package com.example.springpractice.basic.request;

import com.example.springpractice.basic.HelloData;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

/** 클라이언트에서 보낼 메세지
 * {"username":"hello", "age":20}
 * content-type: application/json
 */
@Slf4j
@Controller
public class RequestBodyJsonController {
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 버전이 up되는 과정을 보면서 상위버전이 어떻게 돌아가는 것인지 볼 수 있음

    @PostMapping("/request-body-json-v1")
    public void requestBodyJsonV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody={}", messageBody);
        HelloData data = objectMapper.readValue(messageBody, HelloData.class);
        log.info("username={}, age={}", data.getUsername() , data.getUsername());

        response.getWriter().write("ok");
    }


    // HttpServletRequest ->  @RequestBody 의 과정은 RequestBodyStringController 참고
    /**
     * @RequestBody
     * HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
     *
     * @ResponseBody
     * - 모든 메서드에 @ResponseBody 적용
     * - 메시지 바디 정보 직접 반환(view 조회X)
     * - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
     */
    @ResponseBody
    @PostMapping("/request-body-json-v2")
    public String requestBodyJsonV2(@RequestBody String messageBody) throws IOException {

        log.info("messageBody={}", messageBody);
        HelloData data = objectMapper.readValue(messageBody, HelloData.class);
        log.info("username={}, age={}", data.getUsername() , data.getAge());

        return "OK";
    }


    @ResponseBody
    @PostMapping("/request-body-json-v3")
    public String requestBodyJsonV3(HttpEntity<HelloData> httpEntity) { // @RequestBody 에 직접 만든 객체를 지정할 수 있음
        HelloData data = httpEntity.getBody();
        log.info("messageBody={}", data);
        log.info("username={}, age={}", data.getUsername(), data.getAge());
        return "OK";
    }

    /**
     * @RequestBody 생략 불가능(@ModelAttribute 로 적용되어 버림)
     * HttpMessageConverter 사용 -> MappingJackson2HttpMessageConverter (content-type:application/json)
     */
    @ResponseBody
    @PostMapping("/request-body-json-v4")
    public String requestBodyJsonV4(@RequestBody HelloData data) { // @RequestBody 에 직접 만든 객체를 지정할 수 있음

        log.info("username={}, age={}", data.getUsername() , data.getAge());
        return "OK";
    }


    /**
     * @RequestBody 요청: JSON 요청 ->  HTTP 메시지 컨버터 -> 객체
     *
     * @ResponseBody 응답: 객체 -> HTTP 메시지 컨버터 -> JSON 응답
     * 응답의 경우에도 @ResponseBody 를 사용하면 해당 객체를 HTTP 메시지 바디에 직접 넣어줄 수 있음 (HttpEntity 로도 사용 가능)
     *
     * @ResponseBody 적용
     * - 메시지 바디 정보 직접 반환(view 조회X)
     *  - HttpMessageConverter 사용 -> MappingJackson2HttpMessageConverter 적용(Accept:
     * application/json)
     */
    @ResponseBody
    @PostMapping("/request-body-json-v5")
    public HelloData requestBodyJsonV5(@RequestBody HelloData data) { // @RequestBody 에 직접 만든 객체를 지정할 수 있음

        log.info("username={}, age={}", data.getUsername() , data.getAge());
        return data;
    }





}
