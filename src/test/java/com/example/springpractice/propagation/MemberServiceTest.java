package com.example.springpractice.propagation;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.UnexpectedRollbackException;

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
     * joinV1 안에서 하나의 트랜잭션만 작동하는 상황
     * MemberService    @Transactional:ON
     * MemberRepository @Transactional:OFF
     * LogRepository    @Transactional:OFF
     */
    @Test
    void singleTx() {
        //given
        String username = "singleTx_success";

        //when
        memberService.joinV1(username);

        //then
        Assertions.assertTrue(memberRepository.findByUsername(username).isPresent());
        Assertions.assertTrue(logRepository.find(username).isPresent());

    }

    /**
     * joinV1 안에서 하나의 물리 트랜잭션으로 작동하는 상황 (트랜잭션 전파)
     * MemberService    @Transactional:ON
     * MemberRepository @Transactional:ON
     * LogRepository    @Transactional:ON
     */
    @Test
    void outerTxOn_success() {
        //given
        String username = "outerTxOn_success";

        //when
        memberService.joinV1(username);

        //then
        Assertions.assertTrue(memberRepository.findByUsername(username).isPresent());
        Assertions.assertTrue(logRepository.find(username).isPresent());

    }

    /**
     * joinV1 안에서 하나의 물리 트랜잭션으로 작동하는 상황 (트랜잭션 전파)
     * MemberService    @Transactional:ON
     * MemberRepository @Transactional:ON
     * LogRepository    @Transactional:ON Exception
     *
     * LogRepository 에서 예외발생 시 AOP 프록시에도 예외가 던져지는 상황  -> 전체 롤백
     * LogRepository ->  AOP 프록시(LogRepository) -> MemberService -> AOP 프록시(MemberService) -> 클라이언트
     */
    @Test
    void outerTxOn_fail() {
        //given
        String username = "outerTxOn_로그예외";

        //when
        // 전체 롤백
        assertThatThrownBy(() -> memberService.joinV1(username))
                .isInstanceOf(RuntimeException.class);

        //then
        Assertions.assertTrue(memberRepository.findByUsername(username).isEmpty());
        Assertions.assertTrue(logRepository.find(username).isEmpty());

    }

    /**
     * joinV1 안에서 하나의 물리 트랜잭션으로 작동하는 상황 (트랜잭션 전파)
     * MemberService    @Transactional:ON
     * MemberRepository @Transactional:ON
     * LogRepository    @Transactional:ON Exception
     *
     * LogRepository 에서 발생한 예외를 MemberService에서 catch 했으나...
     *
     */
    @Test
    void recoverException_fail() {
        //given
        String username = "recoverException_fail_로그예외";

        //when
        // 1. MemberService 에서 예외처리했으나 이미 예외가 터지는 시점에 트랜잭션 동기화 매니저에 rollbackOnly 로 체크
        // 2. joinV2에서는 예외가 잡혔으니까 AOP 프록시(MemberService) 까지 예외전달이 안됨 -> 정상인 줄 알고 커밋요청을 하게됨
        // 3. 이미 rollbackOnly 체크 됐는데, 커밋요청을 하는 상황 -> UnexpectedRollbackException 발생
        // 4. UnexpectedRollbackException 전달됨 -> 전체 롤백
        assertThatThrownBy(()-> memberService.joinV2(username))
                .isInstanceOf(UnexpectedRollbackException.class);


        //then
        Assertions.assertTrue(memberRepository.findByUsername(username).isEmpty());
        Assertions.assertTrue(logRepository.find(username).isEmpty());

    }

}