package com.example.springpractice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // index.html이 있으나 정적파일보다 Contoreller가 우선
    @GetMapping("/")
    public String home(){
        return "home";
    }
}
