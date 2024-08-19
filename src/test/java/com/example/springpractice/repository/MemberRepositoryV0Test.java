package com.example.springpractice.repository;

import com.example.springpractice.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

class MemberRepositoryV0Test {

    MemberRepositoryV0 memberRepositoryV0 = new MemberRepositoryV0();


    // primary key (member_id) 로 돼있고, 아직 db를 비우는 메서드 구현은 안했기때문에
    // 다시 실행하려면 h2 db에서 delete from member 을 먼저 해주자. 
    @Test
    void crud() throws SQLException {
        Member member = new Member("회원1",1_000_000);
        memberRepositoryV0.save(member);
    }
}