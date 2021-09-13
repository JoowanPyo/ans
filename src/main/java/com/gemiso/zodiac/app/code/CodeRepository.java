package com.gemiso.zodiac.app.code;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CodeRepository extends JpaRepository<Code, Long> , QuerydslPredicateExecutor<Code> {

    @Query("select max(a.cdOrd) from Code a where a.hrnkCdId = 0")
    Optional<Integer> findHrnkOrd();

    @Query("select max(a.cdOrd) from Code a where a.hrnkCdId =:hrnkCd")
    Optional<Integer> findOrd(@Param("hrnkCd")Long hrnkCd);

    @Query("select a from Code a where a.cdId =:cdId and a.delYn = 'N'")
    Optional<Code> findByCodeId(@Param("cdId")Long cdId);
}
