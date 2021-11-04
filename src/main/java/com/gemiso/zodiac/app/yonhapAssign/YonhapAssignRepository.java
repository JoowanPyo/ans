package com.gemiso.zodiac.app.yonhapAssign;

import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface YonhapAssignRepository extends JpaRepository<YonhapAssign, Long>, QuerydslPredicateExecutor<YonhapAssign> {
}
