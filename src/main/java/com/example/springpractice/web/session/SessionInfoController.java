package com.example.springpractice.web.session;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Slf4j
@RestController
public class SessionInfoController {

    @GetMapping("/session-info")
    public String sessionInfo(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session == null){
            return "세션이 없습니다.";
        }

        // 세션 데이터 출력
        session.getAttributeNames().asIterator()
                .forEachRemaining(name -> log.info("session name ={}, value ={}", name, session.getAttribute(name)));

        log.info("sessionId={}", session.getId());
        log.info("maxInactiveInterval={}", session.getMaxInactiveInterval()); // 세션 유효 시간
        log.info("creationTime={}", new Date(session.getCreationTime())); // 세션 생성일시
        log.info("lastAccessedTime={}", new Date(session.getLastAccessedTime())); //사용자가 마지막으로 접근한 시간
        log.info("isNew={}", session.isNew()); // 새 세션인지, 조회된 세션인지 여부

        return "세션출력";
    }
}
