package com.gemiso.zodiac.app.breakingNewsDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BreakingNewsDtlRepository extends JpaRepository<BreakingNewsDtl, Long>, QuerydslPredicateExecutor<BreakingNewsDtl> {

    @Query("select a from BreakingNewsDtl a where a.breakingNews.breakingNewsId=:breakingNewsId")
    List<BreakingNewsDtl> findDetailList(@Param("breakingNewsId")Long breakingNewsId);

    @Query("select a from BreakingNewsDtl a where a.id=:id")
    Optional<BreakingNewsDtl> findDetail(@Param("id")Long id);
}
