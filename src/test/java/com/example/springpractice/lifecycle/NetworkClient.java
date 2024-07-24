package com.example.springpractice.lifecycle;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class NetworkClient implements InitializingBean, DisposableBean { // 인터페이스를 사용하는 초기화, 종료 방법은 요즘은 거의 사용하지 않는다.

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


    @Override // 프로퍼티의 세팅 끝나면 (의존관계 주입이 끝나면) 자동으로 호출해준다.
    public void afterPropertiesSet() throws Exception {
        System.out.println("afterPropertiesSet() 호출");
        connect();
        call("초기화 연결 메세지");
    }

    @Override // 빈이 종료되기 직전에 자동 호출
    public void destroy() throws Exception {
        System.out.println("destroy() 호출");
        disconnect();
    }
}
