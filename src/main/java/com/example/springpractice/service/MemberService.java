package com.example.springpractice.service;

import com.example.springpractice.domain.Member;
import com.example.springpractice.repository.MemberRepository;
import com.example.springpractice.repository.MemoryMemberRepository;

import java.util.List;
import java.util.Optional;

public class MemberService {
    private final MemberRepository memberRepository = new MemoryMemberRepository();


    /**
     * 회원 가입
     */
    public Long join(Member member){
        // 중복 이름 회원가입 불가
        validateDuplicatedMember(member);

        Member savedMember = memberRepository.save(member);
        return savedMember.getId();
    }

    /**
     * 전체회원 조회
     */
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }


    public Optional<Member> findMember(Long id){
        return memberRepository.findById(id);
    }
    public Optional<Member> findMember(String name){
        return memberRepository.findByName(name);
    }


    private void validateDuplicatedMember(Member member) {
        memberRepository.findByName(member.getName())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }
}
