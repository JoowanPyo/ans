package com.gemiso.zodiac.app.cueSheetMedia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CueSheetMediaRepository extends JpaRepository<CueSheetMedia, Long>, QuerydslPredicateExecutor<CueSheetMedia> {

    @Query("select a from CueSheetMedia a left outer join CueSheetItem b on b.cueItemId = a.cueSheetItem.cueItemId" +
            " where a.cueMediaId = :cueMediaId and a.delYn ='N'")
    Optional<CueSheetMedia> findByCueSheetMedia(@Param("cueMediaId")Long cueMediaId);

    @Query("select a from CueSheetMedia a where a.cueSheetItem.cueItemId =:cueItemId and a.delYn = 'N' ")
    List<CueSheetMedia> findCueMediaList(@Param("cueItemId")Long cueItemId);


    @Query("select a from CueSheetMedia a where a.cueSheetItem.cueItemId =:cueItemId and a.delYn = 'Y' ")
    List<CueSheetMedia> findDeleteCueMediaList(@Param("cueItemId")Long cueItemId);

}
