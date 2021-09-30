package com.gemiso.zodiac.app.capTemplate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CapTemplateRepository extends JpaRepository<CapTemplate, Long>, QuerydslPredicateExecutor<CapTemplate> {

    @Query("select a from CapTemplate a left outer join CapTemplateGrp b on b.tmpltGrpId = a.capTemplateGrp.tmpltGrpId where a.capTmpltId =:capTmpltId and a.delYn = 'N'")
    Optional<CapTemplate> finByCap(@Param("capTmpltId")Long capTmpltId);

    @Query("select max(a.capTmpltOrd) from CapTemplate a")
    Optional<Integer> findOrd();

    @Query("select a from CapTemplate a where a.delYn = 'N' order by a.capTmpltOrd ASC ")
    List<CapTemplate> findCapList();
}
