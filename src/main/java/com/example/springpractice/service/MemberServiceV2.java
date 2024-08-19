package com.example.springpractice.service;

import com.example.springpractice.domain.Member;
import com.example.springpractice.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 트랜잭션 - 파라미터 연동, 풀을 고려한 종료
 */
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV2 {

    private final MemberRepositoryV2 memberRepository;
    private final DataSource dataSource;

    /**
     * 계좌이체
     */
    public void accountTransfer(String fromId, String toId, int money) throws SQLException {

        Connection connection = dataSource.getConnection();

        try{
            connection.setAutoCommit(false);

            // 비즈니스 로직
            bizLogic(connection, fromId, toId, money);
            connection.commit(); // 성공시 커밋

        }catch (Exception e){
            // 중간에 실패시 롤백
            connection.rollback();
            throw new IllegalArgumentException();
        }finally {
            if(connection != null){
                release(connection);
            }
        }



    }

    private void bizLogic(Connection connection, String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepository.findById(connection, fromId);
        Member toMember = memberRepository.findById(connection, toId);

        memberRepository.update(connection, fromMember.getMemberId(), fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(connection, toMember.getMemberId(), toMember.getMoney() + money);
    }

    private static void release(Connection connection) {
        try{
            // 다시 기본값으로 변경 뒤 반납해줘야함 (커넥션 풀 고려)
            connection.setAutoCommit(true);
            connection.close();

        }catch (Exception e){
            log.error("error", e);
        }
    }

    private static void validation(Member toMember) {
        if(toMember.getMemberId().equals("ex")){
            throw new IllegalArgumentException("이체 중 예외발생");
        }
    }
}
