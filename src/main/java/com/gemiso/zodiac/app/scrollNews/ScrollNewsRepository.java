package com.gemiso.zodiac.app.scrollNews;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.Optional;

public interface ScrollNewsRepository extends JpaRepository<ScrollNews, Long>, QuerydslPredicateExecutor<ScrollNews> {

    @Query("select a from ScrollNews a " +
            "left outer join ScrollNewsDetail b on b.scrollNews.scrlNewsId = a.scrlNewsId " +
            "where a.scrlNewsId=:scrlNewsId and a.delYn='N'")
    Optional<ScrollNews> findScrollNews(@Param("scrlNewsId")Long scrlNewsId);


    @Query("select count(a.scrlNewsId) from ScrollNews a where a.brdcDtm between :sdate and :edate and  a.scrlDivCd =:scrlDivCd ")
    Optional<Integer> findScrollNewsCount(@Param("sdate") Date sdate, @Param("edate") Date edate, @Param("scrlDivCd")String scrlDivCd);
}
