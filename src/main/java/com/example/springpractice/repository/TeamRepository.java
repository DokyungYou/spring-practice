package com.example.springpractice.repository;

import com.example.springpractice.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // 생략가능
public interface TeamRepository extends JpaRepository<Team, Long> {
}
