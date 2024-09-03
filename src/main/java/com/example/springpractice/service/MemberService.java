package com.example.springpractice.service;

import com.example.springpractice.api.MemberApiController;
import com.example.springpractice.domain.Member;
import com.example.springpractice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *  @Transactional 에 readOnly = true 를 적용 시
 *  JPA가 조회하는 곳에서는 성능을 조금 더 최적화시켜주기때문에 (영속성 컨텍스트를 플러시 하지 않으므로 약간의 성능 향상)
 *  조회만 하는 로직에는 가급적 readOnly = true 을 적용시키자
 */
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberService {

//    @Autowired // 필드 자동주입은 지양
//    private MemberRepository memberRepository;

    private final MemberRepository memberRepository;

    /**
     * 회원가입
     */
    @Transactional // readOnly = false
    public Long join(Member member){

        validateDuplicateMember(member);

        // 실무에서는 검증 로직이 있어도 멀티 쓰레드 상황을 고려해서 회원 테이블의 회원명 컬럼에 유니크 제약 조건을 추가하는 것이 안전
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }

    }

    /**
     * 회원 조회
     */
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }

    @Transactional
    public void updateMember(Long id, String name) {

        // 영속성 컨텍스트의 관리대상이 된다. (영속상태)
        Member findMember = memberRepository.findOne(id);
        findMember.setName(name);
        // 변경 감지 -> 자동으로 업데이트 쿼리
        // 트랜잭션 aop 가 끝나는 시점에 트랜잭션 커밋 후 -> JPA 가 플러시, 영속성 컨텍스트 커밋
    }
}
