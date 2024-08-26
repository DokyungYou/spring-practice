package com.example.springpractice.propagation;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    LogRepository logRepository;


    /**
     * joinV1 안에서 별개의 트랜잭션으로 작동하는 상황
     * MemberService    @Transactional:OFF
     * MemberRepository @Transactional:ON
     * LogRepository    @Transactional:ON
     */
    @Test
    void outerTxOff_success() {
        //given
        String username = "outerTxOff_success";

        //when
        memberService.joinV1(username);

        //then
        Assertions.assertTrue(memberRepository.findByUsername(username).isPresent());
        Assertions.assertTrue(logRepository.find(username).isPresent());

    }

    /**
     * joinV1 안에서 별개의 트랜잭션으로 작동하는 상황
     * MemberService    @Transactional:OFF
     * MemberRepository @Transactional:ON
     * LogRepository    @Transactional:ON Exception
     */
    @Test
    void outerTxOff_fail() {
        //given
        String username = "outerTxOff_로그예외";

        //when
        //joinV1 내부에서 호출한 memberRepository.save() 는 커밋, logRepository.save()는 롤백인 상황
        assertThatThrownBy(() -> memberService.joinV1(username))
                .isInstanceOf(RuntimeException.class);

        //then
        Assertions.assertTrue(memberRepository.findByUsername(username).isPresent());
        Assertions.assertTrue(logRepository.find(username).isEmpty());

    }

    /**
     * joinV1 안에서 하나의 트랜잭션으로 작동하는 상황
     * MemberService    @Transactional:ON
     * MemberRepository @Transactional:OFF
     * LogRepository    @Transactional:OFF
     */
    @Test
    void singleTx() {
        //given
        String username = "outerTxOff_success";

        //when
        memberService.joinV1(username);

        //then
        Assertions.assertTrue(memberRepository.findByUsername(username).isPresent());
        Assertions.assertTrue(logRepository.find(username).isPresent());

    }

}