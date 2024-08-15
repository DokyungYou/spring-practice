package com.example.springpractice.exception.api;

import com.example.springpractice.exception.custom.MemberException;
import com.example.springpractice.exception.exhandler.ErrorResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api2")
public class ApiExceptionV2Controller {

    /**
     * @ControllerAdvice 에 해당 핸들러들을 등록하지 않을 경우
     * 같은 컨트롤러 내에서 발생한 예외만 처리해준다.
     * 이 부분 다시 실습하고싶으면 ExControllerAdvice 주석처리 하고 진행
     */

    /**
     *  - 의도한 바는 정상흐름으로 바꾸되 응답 상태코드는 200이 아닌 400
     *  - 해당 ExceptionHandlerExceptionResolver 에서 예외를 처리하고, 예외가 서블릿 컨테이너까지 올라가지 않음
     *    이전 실습처럼 서블릿 컨테이너까지 올라간 뒤 다시 내부 요청을 하는 것이 아닌 여기서 흐름이 끝남
     *    여기서 정상적인 흐름으로 반환을 한다.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 상태코드를 지정안해주면 200으로 나감
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandler(IllegalArgumentException e){
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("BAD", e.getMessage());
    }


    //@ExceptionHandler(MemberException.class)
    @ExceptionHandler // value 생략 가능
    public ResponseEntity<ErrorResult> memberHandler(MemberException e){
        log.error("[exceptionHandler] ex", e);
        return new ResponseEntity<>(new ErrorResult("MEMBER-EX", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * 다른 핸들러에서 처리하지 못한 예외는 최상위예외인 Exception을 처리하는 핸들러로 처리하게 된다.
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandler(Exception e){
        log.error("[exceptionHandler] ex", e); // 로그에 예외객체 그대로 넣으면 콘솔에 예외발생한 것처럼 노출됨
        return new ErrorResult("EX", "내부 오류");
    }

    @GetMapping("/members/{id}")
    public MemberDto getMember(@PathVariable("id") String id){
        if(id.equals("ex")){
            throw new RuntimeException("잘못된 사용자");
        }
        if(id.equals("bad")){
            throw new IllegalArgumentException("잘못된 입력 값입니다!"); // 본래 500으로 전달될 것을 400으로 바꿀 것이다.
        }
        if(id.equals("member-ex")){
            throw new MemberException("사용자 오류");
        }
        return new MemberDto(id, "이름");
    }

    @Getter
    @Setter
    @AllArgsConstructor
    static class MemberDto {
        private String memberId;
        private String name;
    }
}
