package com.example.springpractice;


import com.example.springpractice.domain.Item;
import com.example.springpractice.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@Slf4j
@RequiredArgsConstructor
public class TestDataInit {

    private final ItemRepository itemRepository;

    /**
     * 실습을 메모리 기반으로 할 예정이기때문에 (서버를 내리면 데이터가 전부 제거됨)
     * 프로젝트 재 실행시에 필요한 데이터들 초기화하게끔 하였음
     * 
     * 예전 실습과 다르게 @PostConstruct 를 사용하지 않는 이유:
     * PostConstruct 는 AOP 같은 부분이 처리되지 않은 시점에 호출될 가능성이 있음
     */
    @EventListener(ApplicationReadyEvent.class) // 스프링 컨테이너가 초기화를 전부 끝내고, 실행 준비가 됐을 때 발생하는 이벤트
    public void initData() {
        log.info("test data init");
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }

}
