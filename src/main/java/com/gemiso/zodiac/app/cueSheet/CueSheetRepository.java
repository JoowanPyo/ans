package com.gemiso.zodiac.app.cueSheet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CueSheetRepository extends JpaRepository<CueSheet, Long>, QuerydslPredicateExecutor<CueSheet> {

    @Query("select a from CueSheet a left outer join CueSheetItem b on b.cueSheet.cueId = a.cueId " +
            "left outer join Article c on c.artclId = b.article.artclId " +
            "left outer join ArticleCap d on d.article.artclId = c.artclId " +
            "left outer join AnchorCap e on e.article.artclId = c.artclId " +
            "where a.cueId =:cueId and a.delYn ='N'")
    Optional<CueSheet> findByCue(@Param("cueId")Long cueId);

    @Query("select a from CueSheet a where a.cueId =:cueId and a.delYn =:del_yn ")
    Optional<CueSheet> findTakerCue(@Param("cueId") Long cueId, @Param("del_yn") String del_yn);



}
