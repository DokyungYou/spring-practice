package com.example.springpractice.repository;

import com.example.springpractice.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.*;


public class MemoryMemberRepository implements MemberRepository {

    // 공유되는 변수일 경우 동시성 문제로 아래와 같이 사용해야하나, 여기선 패스
    // HashMap -> ConcurrentHashMap
    // Long -> AtomicLong
    private static Map<Long, Member> store = new HashMap<>(); // 현재 db를 대신하는 임시 메모리저장소로 사용
    private static long sequence = 0L;

    @Override
    public Member save(Member member) { // name은 이미 정하고 넘어온 상태
        member.setId(++sequence);
        store.put(member.getId(), member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Member> findByName(String name) {

//        for (Member member : store.values()) {
//            if(member.getName().equals(name)) return Optional.of(member);
//        }


        // 끝까지 돌렸는데 없으면 옵셔널에 null이 포함되어 반환
        return store.values().stream()
                .filter(member -> member.getName().equals(name))
                .findAny(); //Optional<T> findAny()
    }

    @Override
    public List<Member> findAll() {
       return new ArrayList<>(store.values());
    }


    @Override
    public void clear() {
        store.clear();
    }
}
