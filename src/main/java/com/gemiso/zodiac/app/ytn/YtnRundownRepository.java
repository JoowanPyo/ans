package com.gemiso.zodiac.app.ytn;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface YtnRundownRepository extends JpaRepository<YtnRundown, Long>, QuerydslPredicateExecutor<YtnRundown> {

    @Query("select a from YtnRundown a where a.contId =:contId")
    List<YtnRundown> findByYtn(@Param("contId")String contId);
}
