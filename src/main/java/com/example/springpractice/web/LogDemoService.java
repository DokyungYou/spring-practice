package com.example.springpractice.web;

import com.example.springpractice.common.MyLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogDemoService {

    private final ObjectProvider<MyLogger> myLoggerProvider;
    public void logic(String id) {
        MyLogger myLogger = myLoggerProvider.getObject(); // 따로 호출해도 같은 HTTP 요청일 시 같은 bean이 반환
        myLogger.log("service id: " + id);
    }
}
