package com.example.springpractice.repository;

import com.example.springpractice.dto.MemberSearchCondition;
import com.example.springpractice.dto.MemberTeamDto;

import java.util.List;

public interface MemberRepositoryCustom {
    List<MemberTeamDto> search(MemberSearchCondition condition);
}
