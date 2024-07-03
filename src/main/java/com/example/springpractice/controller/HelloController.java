package com.example.springpractice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("hello")
    public String hello(Model model){
        // attributeName 은 key, attributeValue는 값이라고 보면 됨
        model.addAttribute("data","hello!!");

        return "hello"; // resources:templates/hello.html
    }
}
