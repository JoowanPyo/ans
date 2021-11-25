package com.gemiso.zodiac.app.scrollNewsDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ScrollNewsDetailRepository extends JpaRepository<ScrollNewsDetail, Long>, QuerydslPredicateExecutor<ScrollNewsDetail> {

    @Query("select a from ScrollNewsDetail a where a.scrollNews.scrlNewsId=:scrlNewsId")
    List<ScrollNewsDetail> findDetailsList(@Param("scrlNewsId")Long scrlNewsId);

    @Query("select a from ScrollNewsDetail a where a.id=:id")
    Optional<ScrollNewsDetail> findDetail(@Param("id")Long id);
}
