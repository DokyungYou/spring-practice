package com.example.springpractice.controller;

import com.example.springpractice.dto.MemberSearchCondition;
import com.example.springpractice.dto.MemberTeamDto;
import com.example.springpractice.repository.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberJpaRepository memberJpaRepository;

    @GetMapping("/v1/members")
    public List<MemberTeamDto> searchMember(MemberSearchCondition condition) {
        return memberJpaRepository.search(condition);
    }
}
