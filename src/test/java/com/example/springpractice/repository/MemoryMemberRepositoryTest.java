package com.example.springpractice.repository;

import com.example.springpractice.domain.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class MemoryMemberRepositoryTest {
    MemberRepository memberRepository = new MemoryMemberRepository();

    //순서와 상관없이 메소드별로 다 따로 독립적으로 동작할 수 있게 설계해야한다.
    //공용데이터는 지워주어야한다.

    // 메서드 실행이 끌날 때마다 호출 (콜백 메서드라 보면 된다)
    @AfterEach
    public void afterEach(){
        memberRepository.clear();
    };

    @Test
    public void save(){
        Member member = new Member();
        member.setName("You");

        memberRepository.save(member);

        Member result = memberRepository.findById(member.getId()).get();
       // Assertions.assertEquals(member, result); // assertEquals(Object expected, Object actual)
        assertThat(member).isEqualTo(result);
    }

    @Test
    public void findByName(){
        Member member1 = new Member();
        member1.setName("Do");
        memberRepository.save(member1);

        Member member2 = new Member();
        member2.setName("Kim");
        memberRepository.save(member2);

        Member result1 = memberRepository.findByName(member1.getName()).get();
        assertThat(result1).isEqualTo(member1);
        assertThat(result1).isNotEqualTo(member2);
    }

    @Test
    public void findAll(){
        Member member1 = new Member();
        member1.setName("Do");
        memberRepository.save(member1);

        Member member2 = new Member();
        member2.setName("Kim");
        memberRepository.save(member2);

        List<Member> result = memberRepository.findAll();
        assertThat(result.size()).isEqualTo(2);
    }


}
