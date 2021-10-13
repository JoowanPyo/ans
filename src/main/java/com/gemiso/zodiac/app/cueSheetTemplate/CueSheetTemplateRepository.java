package com.gemiso.zodiac.app.cueSheetTemplate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CueSheetTemplateRepository extends JpaRepository<CueSheetTemplate, Long>, QuerydslPredicateExecutor<CueSheetTemplate> {

    @Query("select a from CueSheetTemplate a left outer join Program b on b.brdcPgmId = a.program.brdcPgmId " +
            "where a.cueTmpltId = :cueTmpltId and a.delYn = 'N'")
    Optional<CueSheetTemplate> findCueTemplate(@Param("cueTmpltId")Long cueTmpltId);
}
