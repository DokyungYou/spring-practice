package com.example.springpractice.controller;

public class MemberForm {
    
    // <input type="text" id="name" name="name" placeholder="이름을 입력하세요"> 의 name을 보고 매칭
    // 스프링이 setName을 통해 값을 넣어준다
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
