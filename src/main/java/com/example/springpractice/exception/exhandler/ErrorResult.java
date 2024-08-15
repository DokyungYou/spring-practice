package com.example.springpractice.exception.exhandler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@AllArgsConstructor
public class ErrorResult {
    private String code;
    private String message;
}
