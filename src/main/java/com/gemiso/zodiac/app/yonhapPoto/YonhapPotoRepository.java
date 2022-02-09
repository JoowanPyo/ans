package com.gemiso.zodiac.app.yonhapPoto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface YonhapPotoRepository extends JpaRepository<YonhapPoto, Long>, QuerydslPredicateExecutor<YonhapPoto> {

    @Query("select a from YonhapPoto a where a.contId =:condId")
    List<YonhapPoto> findByYonhapPost(@Param("condId")String condId);
}
