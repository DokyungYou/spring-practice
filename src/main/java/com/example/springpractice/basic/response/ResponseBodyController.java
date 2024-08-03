package com.example.springpractice.basic.response;

import com.example.springpractice.basic.HelloData;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;

@Slf4j
@Controller
//@ResponseBody // 클래스에도 붙일 수 있음 (모든 메서드에 적용)
//@RestController // @Controller + @ResponseBody
public class ResponseBodyController {

    @RequestMapping("/response-body-string-v1")
    public void responseBodyV1(HttpServletResponse response) throws IOException {
        response.getWriter().write("ok");
    }

    @RequestMapping("/response-body-string-v2")
    public ResponseEntity<String> responseBodyV2() {
        return new ResponseEntity<String>("OK", HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping("/response-body-string-v3")
    public String responseBodyV3() {
        return "OK";
    }

    @GetMapping("/response-body-json-v1")
    public ResponseEntity<HelloData> responseBodyJsonV1(){
        HelloData helloData = new HelloData();
        helloData.setUsername("이름");
        helloData.setAge(100);

        return new ResponseEntity<>(helloData, HttpStatus.CREATED);
    }


    // 어노테이션이기 때문에 응답코드를 동적으로 설정할 수 없다. (동적으로 할려면 ResponseEntity 사용)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @GetMapping("/response-body-json-v2")
    public HelloData responseBodyJsonV2(){
        HelloData helloData = new HelloData();
        helloData.setUsername("이름");
        helloData.setAge(100);

        return helloData;
    }
}
