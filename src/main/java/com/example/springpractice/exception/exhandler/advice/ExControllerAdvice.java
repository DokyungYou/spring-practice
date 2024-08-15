package com.example.springpractice.exception.exhandler.advice;

import com.example.springpractice.exception.custom.MemberException;
import com.example.springpractice.exception.exhandler.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


//@ControllerAdvice(annotations = RestController.class)
//@ControllerAdvice(annotations = Controller.class)
//@RestControllerAdvice(basePackages = "com.example.springpractice.exception.api")
@RestControllerAdvice // 대상을 지정하지 않으면 글로벌 적용
@Slf4j
public class ExControllerAdvice {

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

}
