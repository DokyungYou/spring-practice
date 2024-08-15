package com.example.springpractice.type;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode // 모든 필드의 값이 같다면 참
@AllArgsConstructor
public class IpPort {

    private String ip;
    private int port;
}
