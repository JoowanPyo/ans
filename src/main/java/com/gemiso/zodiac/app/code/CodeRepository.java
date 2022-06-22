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

    @Query("select a from Code a where a.cdId =:cdId and a.delYn = 'N' ")
    Optional<Code> findByCodeId(@Param("cdId")Long cdId);

    @Query("select a from Code a where a.hrnkCdId = :artclTypCd and a.delYn = 'N' and a.useYn = 'Y' ")
    List<Code> findArticleTypeCD(@Param("artclTypCd")String artclTypCd);

    @Query("select a from Code a where a.hrnkCdId IN (:underArtclTypCd) and a.useYn = 'Y' ")
    List<Code> findUnderArticleTypeCD(@Param("underArtclTypCd")String[] underArtclTypCd);

    @Query("select a from Code a where a.hrnkCdId = :hrnkCd and a.useYn = 'Y'")
    List<Code> findTakerCode(@Param("hrnkCd")String hrnkCd);

    //and a.useYn = 'Y'뺌 이유는 어차피 관리자밖에 사용안함.
    @Query("select a from Code a where a.hrnkCdId = :hrnkCd and a.delYn = 'N' order by a.cdOrd asc ")
    List<Code> findCodeList(@Param("hrnkCd")String hrnkCd);

    @Query("select a from Code a where a.hrnkCdId = '' and a.delYn = 'N' ")
    List<Code> findHrnkCodeList();
}
