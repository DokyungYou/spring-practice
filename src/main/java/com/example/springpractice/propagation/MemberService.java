package com.example.springpractice.propagation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final LogRepository logRepository;

    /**
     * 회원과 DB로그를 함께 남기는 비즈니스 로직
     * 테스트 코드의 주석 참고 후 Transactional 를 세팅
     *
     * LogRepository 에서 예외발생 시 AOP 프록시에도 예외가 던져지는 상황  -> 롤백
     */
    @Transactional
    public void joinV1(String username){
        Member member = new Member(username);
        Log logMessage = new Log(username);

        log.info(" == memberRepository 호출 시작 ==");
        memberRepository.save(member);
        log.info(" == memberRepository 호출 종료 ==");

        log.info(" == logRepository 호출 시작 ==");
        logRepository.save(logMessage);
        log.info(" == logRepository 호출 종료 ==");
    };


    /**
     * 회원과 DB로그를 함께 남기는 비즈니스 로직
     * DB 로그 저장 시 예외 발생하면 예외 복구
     * 테스트 코드의 주석 참고 후 Transactional 를 세팅
     */
    public void joinV2(String username){
        Member member = new Member(username);
        Log logMessage = new Log(username);

        log.info(" == memberRepository 호출 시작 ==");
        memberRepository.save(member);
        log.info(" == memberRepository 호출 종료 ==");


        log.info(" == logRepository 호출 시작 ==");
        try{
            logRepository.save(logMessage);
        }catch (RuntimeException e){
            log.info("로그 저장 실패. logMessage={}", logMessage.getMessage());
            log.info("정상 흐름 반환");
        }
        log.info(" == logRepository 호출 종료 ==");
    };
}
