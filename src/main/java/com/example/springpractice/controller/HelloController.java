package com.example.springpractice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}
