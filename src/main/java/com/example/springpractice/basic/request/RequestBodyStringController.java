package com.example.springpractice.basic.request;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

@Slf4j
@Controller
public class RequestBodyStringController {
    
    // 버전이 up되는 과정을 보면서 상위버전이 어떻게 돌아가는 것인지 볼 수 있음

    @PostMapping("/request-body-string-v1")
    public void requestBodyStringV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream inputStream = request.getInputStream();

        // InputStream 을 통해 HTTP 메세지 바디의 데이터를 직접 읽을 수 있다.
        // stream은 바이트코드이기때문에 인코딩을 항상 지정해줘야한다.
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        log.info("messageBody = {}", messageBody);
        response.getWriter().write("OK");
    }


    /** Input, Output 스트림, Reader
     * InputStream(Reader): HTTP 요청 메시지 바디의 내용을 직접 조회
     * OutputStream(Writer): HTTP 응답 메시지의 바디에 직접 결과 출력
     */
    @PostMapping("/request-body-string-v2")
    public void requestBodyStringV2(InputStream inputStream, Writer writer) throws IOException {

        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        log.info("messageBody = {}", messageBody);
        writer.write("OK");
    }


    /** HttpEntity
     * HTTP header, body 정보를 편리하게 조회
     * - 메시지 바디 정보를 직접 조회(@RequestParam X, @ModelAttribute X) - 요청 파라미터를 조회하는 기능과 관계 X
     * - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
     *
     * 응답에서도 HttpEntity 사용 가능
     * - 메시지 바디 정보 직접 반환(view 조회X)
     * - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
     */
    @PostMapping("/request-body-string-v3")
    public HttpEntity<String> requestBodyStringV3(HttpEntity<String> httpEntity){ //HTTP 메세지 자체를 그대로 주고받는 형식

        String messageBody = httpEntity.getBody();
        log.info("messageBody = {}", messageBody);
        return new HttpEntity<>("OK");
    }

    
    // 이 부분은 후에 더 다룸
    /**
     * RequestEntity , ResponseEntity -> HttpEntity
     *
     * RequestEntity: HttpMethod, url 정보 추가, 요청에서 사용
     * ResponseEntity: HTTP 상태 코드 설정 가능, 응답에서 사용
     */
    @PostMapping("/request-body-string-v3_2")
    public ResponseEntity<String> requestBodyStringV3_2(RequestEntity<String> httpEntity){

        String messageBody = httpEntity.getBody();
        log.info("messageBody = {}", messageBody);
        return new ResponseEntity<String>("ok message", HttpStatus.CREATED); // 201
    }


    /** @RequestBody, @ResponseBody
     * @RequestBody: HTTP 메세지 바디 정보를 편리하게 조회 (헤더 정보가 필요하다면 HttpEntoty나 @RequestHeader 사용)
     * @ResponseBody: 응답결과를 HTTP 메세지 바디에 직접 담아서 전달가능 (view 사용 X)
     */
    @ResponseBody
    @PostMapping("/request-body-string-v4")
    public String requestBodyStringV4(@RequestBody String messageBody){
        
        log.info("messageBody = {}", messageBody);
        return "Ok";
    }
}
