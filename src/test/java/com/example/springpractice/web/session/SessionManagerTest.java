package com.example.springpractice.web.session;

import com.example.springpractice.domain.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

class SessionManagerTest {

    SessionManager sessionManager = new SessionManager();

    @Test
    void sessionTest() {

        /** 세션 생성 */
        // HttpServletResponse 는 인터페이스이고 구현체가 여러개여서 테스트가 어렵다.
        // 이때 스프링에서 제공해주는 Mock 객체 사용가능
        MockHttpServletResponse response = new MockHttpServletResponse();
        Member member = new Member();

        sessionManager.createSession(member, response);

        // 여기서부터는 웹 브라우저의 요청이라 가정한다.
        /** 요청에 응답 쿠키 저장 */
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(response.getCookie(SessionManager.SESSION_COOKIE_NAME));

        /** 세션 조회 */
        Object result = sessionManager.getSession(request);
        assertThat(result).isEqualTo(member);
        
        /** 세션 만료 */
        sessionManager.expire(request);
        Object expiredSession = sessionManager.getSession(request);
        assertThat(expiredSession).isNull();

    }

}