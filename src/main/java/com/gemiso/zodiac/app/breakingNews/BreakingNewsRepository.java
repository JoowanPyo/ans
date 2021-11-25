package com.gemiso.zodiac.app.breakingNews;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BreakingNewsRepository extends JpaRepository<BreakingNews, Long>, QuerydslPredicateExecutor<BreakingNews> {

    @Query("select a from BreakingNews a where a.breakingNewsId =:breakingNewsId and a.delYn ='N'")
    Optional<BreakingNews> findBreakingNews(@Param("breakingNewsId")Long breakingNewsId);
}
