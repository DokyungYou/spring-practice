package com.example.springpractice.repository;

import com.example.springpractice.entity.Member;

import java.util.List;

public interface MemberRepositoryCustom {
    List<Member> findMemberCustom();
}
