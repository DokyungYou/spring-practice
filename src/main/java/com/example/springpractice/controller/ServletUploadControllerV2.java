package com.example.springpractice.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

@Slf4j
@Controller
@RequestMapping("servlet/v2")
public class ServletUploadControllerV2 {

    @Value("${file.dir}")
    private String fileDir;


    @GetMapping("/upload")
    public String newFile(){
        return "upload-form";
    }


    /**
     * 서블릿이 제공하는 Part는 편하지만, HttpServletRequest 를 사용해야하며,
     * 추가로 파일 부분만 구분하려면 여러가지 코드가 필요
     */
    @PostMapping("/upload")
    public String saveFileV1(HttpServletRequest request) throws ServletException, IOException {
        log.info("request={}", request);
        String itemName = request.getParameter("itemName");
        log.info("itemName={}", itemName);

        Collection<Part> parts = request.getParts();
        log.info("parts ={}", parts);

        for (Part part : parts) {
            log.info("==== PART ====");
            log.info("name={}",  part.getName());

            // part도 header 와 body로 구분 됨
            Collection<String> headerNames = part.getHeaderNames();
            for (String headerName : headerNames) {
                log.info("headerName ={}: {}", headerName, part.getHeader(headerName));
            }

            // 편의 메서드
            // content-disposition; fileName
            log.info("submittedFileName={}", part.getSubmittedFileName());  // 클라이언트가 전달한 파일명
            log.info("size={}", part.getSize()); //part body size

            //데이터 읽기
            InputStream inputStream = part.getInputStream();  // part 의 전송 데이터를 읽을 수 있음

            // binary 데이터를 문자로 바꾸건, 문자를 binary로 바꾸 건 Charset 을 정해줘야한다.
            String body = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            log.info("body={}", body);

            //파일에 저장하기
            if (StringUtils.hasText(part.getSubmittedFileName())) {
                String fullPath = fileDir + part.getSubmittedFileName();
                log.info("파일 저장 fullPath={}", fullPath);
                part.write(fullPath); // 전송된 데이터 저장
            }
        }

        return "upload-form";
    }
}
