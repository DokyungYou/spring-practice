package com.example.springpractice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Item implements Persistable<String> {

    @Id //@GeneratedValue
    private String id;

    @CreatedDate
    private LocalDateTime createdAt;

    public Item(String id) {
        this.id = id;
    }

    /**
     * JPA 식별자 생성 전략이 @Id 만 사용해서 직접 할당이면
     * 이미 식별자 값이 있는 상태로 save() 를 호출 -> merge()사용
     *  merge() 는 우선 DB를 호출해서 값을 확인하고, DB에 값이 없으면 새로운 엔티티로 인지하므로 매우 비효율적
     * 따라서 Persistable를 사용해서 새로운 엔티티 확인 여부를 직접 구현하는 것이 효과적
     */
    @Override
    public boolean isNew() {
        return createdAt == null;
    }
}
