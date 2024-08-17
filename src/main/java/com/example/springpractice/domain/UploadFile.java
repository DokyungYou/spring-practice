package com.example.springpractice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UploadFile {

    /**
     * 서로 다른 고객이 같은 이름의 파일을 업로드하는 경우 충돌 및 덮어쓰여지는 상황이 생길 수 있기때문에,
     * 서버 내부에서 관리하는 별도의 파일명 필요
     */

    private String uploadFileName; // 고객이 업로드한 파일명
    private String storeFileName; // (서버 내부)시스템에 저장한 파일명
}
