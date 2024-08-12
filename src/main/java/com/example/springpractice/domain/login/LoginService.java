package com.example.springpractice.domain.login;

import com.example.springpractice.domain.member.Member;
import com.example.springpractice.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    /** login
     * 회원 조회 -> password 비교 -> 같으면 회원 / 다르면 null 반환
     * null 반환 시 로그인실패
     */
    public Member login(String loginId, String password){
//        Optional<Member> optionalMember = memberRepository.findByLoginId(loginId);
//        if(!optionalMember.isPresent()){
//            return null;
//        }
//        Member member = optionalMember.get();
//        if(!member.getPassword().equals(password)){
//            return null;
//        }
//        return member;

        return memberRepository.findByLoginId(loginId)
                .filter(member -> member.getPassword().equals(password))
                .orElse(null);
    }
}
