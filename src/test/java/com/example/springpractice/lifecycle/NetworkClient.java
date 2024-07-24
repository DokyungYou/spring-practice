package com.example.springpractice.lifecycle;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class NetworkClient { // 해당 빈이 스프링 코드에 의존적이지 않음

    private String url;

    public NetworkClient() {
        System.out.println("생성자 호출 , url: " + url);
        
        // 해당 초기화로직을 InitializingBean 의 afterPropertiesSet() 에서 수행하게끔 변경
//        connect();
//        call("초기화 연결 메세지");
    }

    public void setUrl(String url) {
        this.url = url;
    }

    // 서비스 시작 시 호출
    public void connect(){
        System.out.println("connect: " + url);
    }

    public void call(String message){
        System.out.println("call: " + url + "message: " + message);
    }

    // 서비스 종료 시 호출
    public void disconnect(){
        System.out.println("close: " + url);
    }


    // 프로퍼티의 세팅 후 (의존관계 주입이 끝나면) 콜백함수 용도
    public void init() throws Exception {
        System.out.println(" NetworkClient init() 호출");
        connect();
        call("초기화 연결 메세지");
    }

    // 빈이 종료되기 직전에 자동 호출 용도
    public void close() throws Exception {
        System.out.println("NetworkClient close() 호출");
        disconnect();
    }
}
