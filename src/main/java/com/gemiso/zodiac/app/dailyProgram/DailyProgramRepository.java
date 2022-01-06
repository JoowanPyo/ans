package com.gemiso.zodiac.app.dailyProgram;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface DailyProgramRepository extends JpaRepository<DailyProgram, Long>, QuerydslPredicateExecutor<DailyProgram> {
}
