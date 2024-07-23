package com.example.springpractice.member.service;

import com.example.springpractice.member.Member;
import com.example.springpractice.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

//    @Autowired //의존관계 자동주입 (파라미터의 타입에 맞는 bean을 찾아와서 자동으로 주입해줌)
//    public MemberServiceImpl(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    @Override
    public void signup(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long id) {
        return memberRepository.findById(id);
    }

    // Test용
    public MemberRepository getMemberRepository(){
        return memberRepository;
    }
}
