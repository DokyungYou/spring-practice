package com.example.springpractice.domain.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
public class MemberRepository {

    // 동시성 문제가 고려되어 있지 않음, 실무에서는 ConcurrentHashMap, AtomicLong 사용 고려
    private static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L;

    public Member save(Member member){
        member.setId(++sequence);
        log.info("save: member={}", member);
        store.put(sequence, member);
        return member;
    }

    public List<Member> findAll(){
        return new ArrayList<>(store.values());
    }
    public Member findById(Long id){
        return store.get(id);
    }

    public Optional<Member> findByLoginId(String loginId){
        return findAll().stream()
                .filter(member -> member.getLoginId().equals(loginId))
                .findFirst();

//        for(Member member : findAll()){
//            if(member.getLoginId().equals(loginId)){
//                return Optional.of(member);
//            }
//        }
//        return Optional.empty();
    }

    public void clearStore(){
        store.clear();
    }

}
