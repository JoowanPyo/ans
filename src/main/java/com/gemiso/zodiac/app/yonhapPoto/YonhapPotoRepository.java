package com.gemiso.zodiac.app.yonhapPoto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface YonhapPotoRepository extends JpaRepository<YonhapPoto, Long>, QuerydslPredicateExecutor<YonhapPoto> {
}
