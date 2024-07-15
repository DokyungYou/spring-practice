package com.example.springpractice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller  // Controller:  웹 어플리케이션에서의 첫번째 진입점
public class HelloController {

    @GetMapping("hello")
    public String hello(Model model){
        // attributeName 은 key, attributeValue는 값이라고 보면 됨
        model.addAttribute("data","hello!!");

        return "hello";
        // resources:templates/hello.html
        // 컨트롤러에서 리턴 값으로 문자 반환 시 viewResolver 가 화면을 찾아서 처리
    }

    @GetMapping("hello-mvc") // @RequestParam은 required = true 가 기본값
    public String helloMvc(@RequestParam(value = "name", required = false) String name, Model model){
        model.addAttribute("name", name);
        return "hello-template";
    }

    @GetMapping("hello-string")
    @ResponseBody // http응답 body에 데이터를 직접 넣어주겠다
    public String helloString(@RequestParam(value="name") String name){
        return "hello" + name; // view가 없이 문자 그대로 반환 -> StringConverter 작동
    }

    @GetMapping("hello-api")
    @ResponseBody // HttpMessageConverter 작동 (뷰 없이 바로 httpResponse에 값을 넣어서 반환)
    public Hello helloApi(@RequestParam(value="name") String name){
        return new Hello(name); // 객체로 반환 -> JsonConverter 작동
    }
    static class Hello{
        private String name;

        public Hello(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
