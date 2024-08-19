package com.example.springpractice.service;

import com.example.springpractice.domain.Member;
import com.example.springpractice.repository.MemberRepositoryV1;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;

@RequiredArgsConstructor
public class MemberServiceV1 {

    private final MemberRepositoryV1 memberRepository;

    /**
     * 계좌이체
     */
    public void accountTransfer(String fromId, String toId, int money) throws SQLException {

        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);

        memberRepository.update(fromMember.getMemberId(), fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(toMember.getMemberId(), toMember.getMoney() + money);
    }

    private static void validation(Member toMember) {
        if(toMember.getMemberId().equals("ex")){
            throw new IllegalArgumentException("이체 중 예외발생");
        }
    }
}
