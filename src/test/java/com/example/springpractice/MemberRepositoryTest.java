//package com.example.springpractice;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//class MemberRepositoryTest {
//
//    @Autowired
//    MemberRepository memberRepository;
//
//
//    @Test
//    @Transactional // 엔티티 매니저를 통한 모든 데이터 변경은 항상 트랜잭션 안에서 이루어져야 한다.
//    @Rollback(false) // 테스트메서드에서의 Transactional 설정은 모두 롤백되기때문에 실습을 위해 추가
//    public void testMember(){
//        //given
//        Member member = new Member();
//        member.setUsername("멤버A");
//
//        //when
//        memberRepository.save(member);
//
//        //then
//        Member findMember = memberRepository.find(member.getId());
//        assertThat(findMember.getId()).isEqualTo(member.getId());
//        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
//
//        // Member 객체는 EqualsHashCode 재정의하지 않은 상태이기때문에 isEqualTo -> == 비교
//        // 현재 같은 트랜잭션 안에서 저장, 조회했음 (영속성 컨텍스트가 같은 상태)
//        // 같은 영속성 컨텍스트 안에서는 id 값이 같으면 같은 엔티티로 식별
//        assertThat(findMember).isEqualTo(member);
//    }
//}