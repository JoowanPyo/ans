package com.gemiso.zodiac.app.code;

import com.gemiso.zodiac.app.code.dto.CodeDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CodeRepository extends JpaRepository<Code, Long> , QuerydslPredicateExecutor<Code> {

    @Query("select max(a.cdOrd) from Code a where a.hrnkCdId = ''")
    Optional<Integer> findHrnkOrd();

    @Query("select max(a.cdOrd) from Code a where a.hrnkCdId =:hrnkCd")
    Optional<Integer> findOrd(@Param("hrnkCd")String hrnkCd);

    @Query("select a from Code a where a.cdId =:cdId and a.delYn = 'N'")
    Optional<Code> findByCodeId(@Param("cdId")Long cdId);

    @Query("select a from Code a where a.hrnkCdId = :artclTypCd and a.delYn = 'N'")
    List<Code> findArticleTypeCD(@Param("artclTypCd")Long artclTypCd);

    @Query("select a from Code a where a.hrnkCdId IN (:underArtclTypCd)")
    List<Code> findUnderArticleTypeCD(@Param("underArtclTypCd")String[] underArtclTypCd);
}
