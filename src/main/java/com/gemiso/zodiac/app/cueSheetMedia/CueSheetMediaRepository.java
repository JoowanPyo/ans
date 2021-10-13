package com.gemiso.zodiac.app.cueSheetMedia;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CueSheetMediaRepository extends JpaRepository<CueSheetMedia, Long>, QuerydslPredicateExecutor<CueSheetMedia> {

    @Query("select a from CueSheetMedia a left outer join CueSheetItem b on b.cueItemId = a.cueSheetItem.cueItemId" +
            " where a.cueMediaId = :cueMediaId and a.delYn ='N'")
    Optional<CueSheetMedia> findByCueSheetMedia(@Param("cueMediaId")Long cueMediaId);
}
