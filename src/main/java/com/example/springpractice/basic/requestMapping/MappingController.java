package com.example.springpractice.basic.requestMapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class MappingController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    // 속성으로 HTTP 메서드를 지정하지 않으면 HTTP 메서드와 무관하게 호출
    @RequestMapping({"hello-basic", "/hello-go"}) // 대부분의 속성을 배열로 제공하기에 다중 설정 가능
    public String helloBasic1(){

        log.info("helloBasic");
        return "OK";
    }

    @RequestMapping(method = RequestMethod.GET, value = {"hello-basic2", "/hello-go2"})
    public String helloBasic2(){

        log.info("helloBasic2");
        return "OK";
    }

    // @RequestMapping(method = RequestMethod.GET)
    // 특정 RequestMethod 에 맞는 축약된 어노테이션들을 제공해준다.
    @GetMapping(value = "/mapping-get-v2")
    public String mappingGetV2(){
        log.info("mapping-get-v2");
        return "OK";
    }

    /**
     * PathVariable 사용 (url 자체에 값이 들어있음)
     * 변수명이 같으면 생략 가능  @PathVariable(이름 생략 가능)
     * @PathVariable("userId") String userId -> @PathVariable String userId
     */
    @GetMapping("/mapping/{userId}")
    public String mappingPath(@PathVariable String userId){ // 변수명이 같기때문에 PathVariable의 이름 생략가능 (@PathVariable 자체를 생략하는 것은 안됨)
        log.info("mappingPath userId = {}",userId);
        return "OK";
    }


    /**
     * PathVariable 사용 다중
     */
    @GetMapping("/mapping/users/{userId}/orders/{orderId}")
    public String mappingPath(@PathVariable String userId,
                              @PathVariable Long orderId) {
        log.info("mappingPath userId={}, orderId={}", userId, orderId);
        return "OK";
    }


    /** 특정 파라미터 조건 매핑
     * 파라미터로 추가 매핑 (아래와 같이 여러가지 표현가능) - 잘 사용하지는 않으니까 알아만 두자
     * params="mode",
     * params="!mode"
     * params="mode=debug"
     * params="mode!=debug" (! = )
     * params = {"mode=debug","data=good"}
     */
    @GetMapping(value = "/mapping-param", params = "mode=debug") // 특정 파라미터 정보가 있어야 호출가능
    public String mappingParam() {
        log.info("mappingParam");
        return "OK";
    }

    
    // TODO: 아래에 나오는 부분들은 HTTP 완강하고 다시 복습해보기

    /** 특정 헤더 조건 매핑
     * 특정 헤더로 추가 매핑 - 나중에 사용할 일이 있을 때 파보기 (지금은 이정도로)
     * headers="mode",
     * headers="!mode"
     * headers="mode=debug"
     * headers="mode!=debug" (! = )
     */
    @GetMapping(value = "/mapping-header", headers = "mode=debug")
    public String mappingHeader(){
        log.info("mappingHeader");
        return "OK";
    }

    /** 미디어 타입 조건 매핑 - HTTP 요청 Content-Type, consume
     * Content-Type 헤더 기반 추가 매핑 Media Type
     * consumes="application/json"
     * consumes="!application/json"
     * consumes="application/*"
     * consumes="*\/*"
     * MediaType.APPLICATION_JSON_VALUE
     */
    @PostMapping(value = "/mapping-consume", consumes = MediaType.APPLICATION_JSON_VALUE) //"application/json"
    public String mappingConsumes() {
        log.info("mappingConsumes");
        return "ok";
    }

    /** 미디어 타입 조건 매핑 - HTTP 요청 Accept, produce
     * Accept 헤더 기반 Media Type
     * produces = "text/html"
     * produces = "!text/html"
     * produces = "text/*"
     * produces = "*\/*"
     */
    @PostMapping(value = "/mapping-produce", produces = MediaType.TEXT_HTML_VALUE) // "text/html"
    public String mappingProduces() {
        log.info("mappingProduces");
        return "ok";
    }
}
