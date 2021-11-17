package com.gemiso.zodiac.app.cueSheetHist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CueSheetHistRepository extends JpaRepository<CueSheetHist, Long>, QuerydslPredicateExecutor<CueSheetHist> {

    @Query("select count (a.cueVer) from CueSheetHist a where a.cueSheet.cueId = :cueId")
    int findCueVer(@Param("cueId")Long cueId);

    @Query("select a from CueSheetHist a where a.cueHistId = :cueHistId")
    Optional<CueSheetHist> findCueHist(@Param("cueHistId")Long cueHistId);
}
