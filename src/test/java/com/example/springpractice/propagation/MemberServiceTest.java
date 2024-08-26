package com.example.springpractice.propagation;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired LogRepository logRepository;


    /**
     * MemberService    @Transactional:OFF
     * MemberRepository @Transactional:ON
     * LogRepository    @Transactional:ON
     */
    @Test
    void outerTxOff_success(){
        //given
        String username = "outerTxOff_success";

        //when
        memberService.joinV1(username);

        //then
        Assertions.assertTrue(memberRepository.findByUsername(username).isPresent());
        Assertions.assertTrue(logRepository.find(username).isPresent());

    }
}