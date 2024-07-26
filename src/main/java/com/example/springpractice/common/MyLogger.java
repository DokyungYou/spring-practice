package com.example.springpractice.common;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(value = "request")
public class MyLogger {

    private String uuid;
    private String requestUrl;


    // requestUrl은 bean이 생성되는 시점에는 알 수 없기에 setter로 입력
    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public void log(String message){
        System.out.printf("[%s][%s] %s\n", uuid, requestUrl, message);
    }

    @PostConstruct
    public void init(){
        this.uuid = UUID.randomUUID().toString();
        System.out.printf("[%s] request scope bean created: %s \n", uuid, this);
    }
    @PreDestroy // 프로토타입과 다르게 자동호출 가능
    public void close(){
        System.out.printf("[%s] request scope bean closed: %s \n", uuid, this);
    }
}
